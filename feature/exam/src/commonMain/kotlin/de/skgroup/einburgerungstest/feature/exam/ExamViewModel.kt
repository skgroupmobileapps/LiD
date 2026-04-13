package de.skgroup.einburgerungstest.feature.exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.skgroup.einburgerungstest.core.model.ExamResult
import de.skgroup.einburgerungstest.core.model.ExamSession
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.util.Timer
import de.skgroup.einburgerungstest.domain.usecase.ExamFlowUseCase
import de.skgroup.einburgerungstest.data.repository.SettingsRepository
import de.skgroup.einburgerungstest.tracking.TrackingClient
import de.skgroup.einburgerungstest.tracking.TrackingEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ExamPhase {
    INTRO,       // ExamIntroScreen
    IN_PROGRESS, // ExamQuestionScreen — no feedback
    RESULT       // ExamResultScreen — results + review
}

data class ExamUiState(
    val phase: ExamPhase = ExamPhase.INTRO,
    val session: ExamSession? = null,
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val remainingTimeMs: Long = 60 * 60 * 1000L,
    val result: ExamResult? = null,
    val isLoading: Boolean = false,
    val federalState: FederalState = FederalState.BERLIN
)

class ExamViewModel(
    private val examFlowUseCase: ExamFlowUseCase,
    private val settingsRepository: SettingsRepository,
    private val trackingClient: TrackingClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExamUiState())
    val uiState: StateFlow<ExamUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = settingsRepository.loadSettings()
            _uiState.update { it.copy(federalState = settings.federalState) }
        }
    }

    /**
     * Start a new exam with 33 questions.
     */
    fun startExam() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isLoading = true) }
            val session = examFlowUseCase.startExam(_uiState.value.federalState)
            _uiState.update {
                it.copy(
                    phase = ExamPhase.IN_PROGRESS,
                    session = session,
                    currentQuestionIndex = 0,
                    selectedAnswerIndex = null,
                    remainingTimeMs = session.timeLimitMs,
                    isLoading = false
                )
            }
            trackingClient.track(TrackingEvent.ExamStarted(_uiState.value.federalState.name))
            startTimer(session.timeLimitMs)
        }
    }

    private fun startTimer(totalTimeMs: Long) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            Timer.countdown(totalTimeMs).collect { remaining ->
                _uiState.update { it.copy(remainingTimeMs = remaining) }
                if (remaining <= 0L) {
                    val session = _uiState.value.session
                    if (session != null) {
                        trackingClient.track(
                            TrackingEvent.TimerExpired(
                                answeredQuestions = session.answeredCount,
                                totalQuestions = session.totalQuestions
                            )
                        )
                    }
                    finishExam()
                }
            }
        }
    }

    /**
     * Select an answer (exam mode — no checking, just record).
     */
    fun selectAnswer(questionId: Int, selectedIndex: Int) {
        val session = _uiState.value.session ?: return
        examFlowUseCase.submitAnswer(session, questionId, selectedIndex)
        val question = session.questions.find { it.id == questionId }
        if (question != null) {
            trackingClient.track(
                TrackingEvent.QuestionAnswered(
                    questionId = questionId,
                    isCorrect = selectedIndex == question.correctAnswerIndex,
                    mode = "exam",
                    topic = question.topic.name
                )
            )
        }
        _uiState.update { it.copy(selectedAnswerIndex = selectedIndex) }
    }

    /**
     * Move to the next question.
     */
    fun nextQuestion() {
        val state = _uiState.value
        val session = state.session ?: return
        val nextIndex = state.currentQuestionIndex + 1

        if (nextIndex >= session.totalQuestions) {
            finishExam()
        } else {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = nextIndex,
                    selectedAnswerIndex = session.answers[session.questions[nextIndex].id]
                )
            }
        }
    }

    fun previousQuestion() {
        val state = _uiState.value
        if (state.currentQuestionIndex > 0) {
            val prevIndex = state.currentQuestionIndex - 1
            val session = state.session ?: return
            _uiState.update {
                it.copy(
                    currentQuestionIndex = prevIndex,
                    selectedAnswerIndex = session.answers[session.questions[prevIndex].id]
                )
            }
        }
    }

    /**
     * Finish the exam and show results. No feedback was given during the exam.
     */
    fun finishExam() {
        timerJob?.cancel()
        viewModelScope.launch(Dispatchers.Default) {
            val session = _uiState.value.session ?: return@launch
            val result = examFlowUseCase.finishExam(session)
            trackingClient.track(
                TrackingEvent.ExamCompleted(
                    score = result.correctCount,
                    passed = result.passed,
                    timeSpentSeconds = result.timeSpentMs / 1000,
                    federalState = result.federalState.name
                )
            )
            _uiState.update {
                it.copy(
                    phase = ExamPhase.RESULT,
                    result = result
                )
            }
        }
    }

    fun abandonExam() {
        val session = _uiState.value.session
        if (session != null) {
            val elapsedSeconds = (session.timeLimitMs - _uiState.value.remainingTimeMs).coerceAtLeast(0L) / 1000
            trackingClient.track(
                TrackingEvent.ExamAbandoned(
                    currentQuestionIndex = _uiState.value.currentQuestionIndex,
                    totalQuestions = session.totalQuestions,
                    timeSpentSeconds = elapsedSeconds
                )
            )
        }
        resetExam()
    }

    fun resetExam() {
        timerJob?.cancel()
        _uiState.value = ExamUiState()
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}
