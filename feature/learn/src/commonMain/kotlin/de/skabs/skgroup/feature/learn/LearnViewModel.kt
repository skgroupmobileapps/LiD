package de.skabs.skgroup.feature.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.skabs.skgroup.core.model.FederalState
import de.skabs.skgroup.core.model.Question
import de.skabs.skgroup.core.model.Topic
import de.skabs.skgroup.core.model.TopicProgress
import de.skabs.skgroup.data.repository.SettingsRepository
import de.skabs.skgroup.domain.usecase.AnswerFeedback
import de.skabs.skgroup.domain.usecase.BookmarkUseCase
import de.skabs.skgroup.domain.usecase.FeedbackUseCase
import de.skabs.skgroup.domain.usecase.LearningUseCase
import de.skabs.skgroup.domain.usecase.StatisticsUseCase
import de.skabs.skgroup.tracking.TrackingClient
import de.skabs.skgroup.tracking.TrackingEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LearnUiState(
    val topicProgressList: List<TopicProgress> = emptyList(),
    val currentQuestions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val feedback: AnswerFeedback? = null,
    val bookmarkedQuestions: List<Question> = emptyList(),
    val bookmarkCount: Int = 0,
    val isLoading: Boolean = true,
    val showFeedbackTrigger: Boolean = false
)

class LearnViewModel(
    private val learningUseCase: LearningUseCase,
    private val bookmarkUseCase: BookmarkUseCase,
    private val statisticsUseCase: StatisticsUseCase,
    private val settingsRepository: SettingsRepository,
    private val trackingClient: TrackingClient,
    private val feedbackUseCase: FeedbackUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearnUiState())
    val uiState: StateFlow<LearnUiState> = _uiState.asStateFlow()

    private val federalState: FederalState
        get() = settingsRepository.loadSettings().federalState

    init {
        loadTopics()
    }

    fun loadTopics() {
        viewModelScope.launch(Dispatchers.Default) {
            val topicProgress = statisticsUseCase.getTopicProgressListForUser(federalState)
            val bookmarks = bookmarkUseCase.getBookmarkedQuestions()
            _uiState.update {
                it.copy(
                    topicProgressList = topicProgress,
                    bookmarkedQuestions = bookmarks,
                    bookmarkCount = bookmarks.size,
                    isLoading = false
                )
            }
        }
    }

    fun loadQuestionsForTopic(topic: Topic) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isLoading = true) }
            val questions = learningUseCase.getQuestionsForTopicAndState(topic, federalState)
            val answeredIds = statisticsUseCase.getAnsweredQuestionIds()
            val firstUnansweredIndex = questions.indexOfFirst { !answeredIds.contains(it.id) }.coerceAtLeast(0)
            
            _uiState.update {
                it.copy(
                    currentQuestions = questions,
                    currentQuestionIndex = firstUnansweredIndex,
                    selectedAnswerIndex = null,
                    feedback = null,
                    isLoading = false
                )
            }
        }
    }

    fun loadAllQuestions() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isLoading = true) }
            val questions = learningUseCase.getCandidateQuestions(federalState)
            val answeredIds = statisticsUseCase.getAnsweredQuestionIds()
            val firstUnansweredIndex = questions.indexOfFirst { !answeredIds.contains(it.id) }.coerceAtLeast(0)

            _uiState.update {
                it.copy(
                    currentQuestions = questions,
                    currentQuestionIndex = firstUnansweredIndex,
                    selectedAnswerIndex = null,
                    feedback = null,
                    isLoading = false
                )
            }
        }
    }

    /**
     * Submit answer in learn mode — immediate checking with feedback.
     */
    fun submitAnswer(question: Question, selectedIndex: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val feedback = learningUseCase.submitLearningAnswer(question, selectedIndex)
            trackingClient.track(
                TrackingEvent.QuestionAnswered(
                    questionId = question.id,
                    isCorrect = feedback.isCorrect,
                    mode = "learn",
                    topic = question.topic.name
                )
            )

            val state = _uiState.value
            val isLastQuestion = state.currentQuestionIndex >= state.currentQuestions.size - 1

            // Track topic completion for feedback trigger
            if (isLastQuestion && feedback.isCorrect) {
                feedbackUseCase.recordSuccessfulLearnSession()
                if (feedbackUseCase.shouldShowFeedbackAfterLearning()) {
                    _uiState.update { it.copy(showFeedbackTrigger = true) }
                }
            }

            _uiState.update {
                it.copy(
                    selectedAnswerIndex = selectedIndex,
                    feedback = feedback
                )
            }
        }
    }

    fun nextQuestion() {
        _uiState.update {
            it.copy(
                currentQuestionIndex = (it.currentQuestionIndex + 1).coerceAtMost(it.currentQuestions.size - 1),
                selectedAnswerIndex = null,
                feedback = null
            )
        }
    }

    fun toggleBookmark(questionId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            bookmarkUseCase.toggleBookmark(questionId)
            val isBookmarked = bookmarkUseCase.isBookmarked(questionId)
            trackingClient.track(
                TrackingEvent.BookmarkToggled(
                    questionId = questionId,
                    isBookmarked = isBookmarked
                )
            )
            val bookmarks = bookmarkUseCase.getBookmarkedQuestions()
            _uiState.update {
                it.copy(
                    bookmarkedQuestions = bookmarks,
                    bookmarkCount = bookmarks.size
                )
            }
        }
    }

    fun isBookmarked(questionId: Int): Boolean {
        return bookmarkUseCase.isBookmarked(questionId)
    }

    fun clearFeedbackTrigger() {
        _uiState.update { it.copy(showFeedbackTrigger = false) }
    }
}
