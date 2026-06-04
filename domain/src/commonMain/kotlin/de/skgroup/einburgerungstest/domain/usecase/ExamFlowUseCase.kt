package de.skgroup.einburgerungstest.domain.usecase

import de.skgroup.einburgerungstest.core.model.ExamResult
import de.skgroup.einburgerungstest.core.model.ExamSession
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.WrongAnswer
import de.skgroup.einburgerungstest.core.util.Randomizer
import de.skgroup.einburgerungstest.core.util.Scoring
import de.skgroup.einburgerungstest.data.repository.ExamRepository
import de.skgroup.einburgerungstest.data.repository.ProgressRepository
import de.skgroup.einburgerungstest.data.repository.QuestionRepository
import kotlinx.datetime.Clock

internal data class ExamEvaluation(
    val correctCount: Int,
    val wrongAnswers: List<WrongAnswer>
)

internal fun evaluateExamAnswers(session: ExamSession): ExamEvaluation {
    var correctCount = 0
    val wrongAnswers = mutableListOf<WrongAnswer>()

    session.questions.forEach { question ->
        val selectedIndex = session.answers[question.id]
        when {
            selectedIndex == null -> {
                wrongAnswers.add(
                    WrongAnswer(
                        question = question,
                        selectedAnswerIndex = -1,
                        correctAnswerIndex = question.correctAnswerIndex
                    )
                )
            }
            selectedIndex == question.correctAnswerIndex -> {
                correctCount++
            }
            else -> {
                wrongAnswers.add(
                    WrongAnswer(
                        question = question,
                        selectedAnswerIndex = selectedIndex,
                        correctAnswerIndex = question.correctAnswerIndex
                    )
                )
            }
        }
    }

    return ExamEvaluation(
        correctCount = correctCount,
        wrongAnswers = wrongAnswers
    )
}

/**
 * Use case for managing the exam flow.
 *
 * Exam mode behavior:
 * - No answer checking during the exam
 * - Answers are collected silently
 * - Results shown only at the end (pass/fail + wrong answer review)
 */
class ExamFlowUseCase(
    private val questionRepository: QuestionRepository,
    private val examRepository: ExamRepository,
    private val progressRepository: ProgressRepository
) {
    /**
     * Start a new exam session with 33 randomized questions.
     */
    fun startExam(federalState: FederalState): ExamSession {
        val allQuestions = questionRepository.getAllQuestions()
        val examQuestions = Randomizer.selectExamQuestions(allQuestions, federalState)
        val now = Clock.System.now().toEpochMilliseconds()
        return ExamSession(
            questions = examQuestions,
            startTimeMs = now,
            federalState = federalState
        )
    }

    /**
     * Submit an answer during the exam (no feedback given).
     */
    fun submitAnswer(session: ExamSession, questionId: Int, selectedIndex: Int): ExamSession {
        session.answers[questionId] = selectedIndex
        return session
    }

    /**
     * Finish the exam and calculate the result.
     * Records all answers to progress tracking and saves exam history.
     */
    fun finishExam(session: ExamSession): ExamResult {
        val now = Clock.System.now().toEpochMilliseconds()
        val timeSpent = now - session.startTimeMs

        val evaluation = evaluateExamAnswers(session)

        session.questions.forEach { question ->
            val selectedIndex = session.answers[question.id]
            if (selectedIndex != null) {
                val isCorrect = selectedIndex == question.correctAnswerIndex
                // Record each answer for progress tracking
                progressRepository.recordAnswer(
                    questionId = question.id,
                    selectedIndex = selectedIndex,
                    isCorrect = isCorrect,
                    mode = "exam"
                )
            }
        }

        val totalQuestions = session.totalQuestions
        val wrongCount = evaluation.wrongAnswers.size
        val result = ExamResult(
            totalQuestions = totalQuestions,
            correctCount = evaluation.correctCount,
            wrongCount = wrongCount,
            passed = Scoring.isPassed(evaluation.correctCount, totalQuestions),
            scorePercent = Scoring.calculateScorePercent(evaluation.correctCount, totalQuestions),
            timeSpentMs = timeSpent,
            wrongAnswers = evaluation.wrongAnswers,
            federalState = session.federalState,
            timestampMs = now
        )

        // Save to exam history
        examRepository.saveExamResult(result)

        return result
    }
}
