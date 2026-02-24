package de.skabs.skgroup.domain.usecase

import de.skabs.skgroup.core.model.FederalState
import de.skabs.skgroup.core.model.Question
import de.skabs.skgroup.core.model.Topic
import de.skabs.skgroup.data.repository.ProgressRepository
import de.skabs.skgroup.data.repository.QuestionRepository

/**
 * Result of checking an answer in learn mode.
 */
data class AnswerFeedback(
    val question: Question,
    val selectedIndex: Int,
    val isCorrect: Boolean,
    val correctIndex: Int,
    val explanation: String
)

/**
 * Use case for learning mode.
 *
 * Learn mode behavior:
 * - Each answer is checked immediately
 * - Wrong answers show explanation text
 * - Progress is tracked per question and topic
 */
class LearningUseCase(
    private val questionRepository: QuestionRepository,
    private val progressRepository: ProgressRepository
) {
    /**
     * Get all questions for a specific topic.
     */
    fun getQuestionsForTopic(topic: Topic): List<Question> {
        return questionRepository.getQuestionsByTopic(topic)
    }

    /**
     * Get all candidate questions (300 general + 10 for their state).
     */
    fun getCandidateQuestions(federalState: FederalState): List<Question> {
        return questionRepository.getCandidateQuestions(federalState)
    }

    /**
     * Get all general questions.
     */
    fun getAllQuestions(): List<Question> {
        return questionRepository.getAllQuestions()
    }

    /**
     * Check a learning answer immediately and return feedback.
     * If wrong, the explanation is included for the user to learn.
     */
    fun submitLearningAnswer(question: Question, selectedIndex: Int): AnswerFeedback {
        val isCorrect = selectedIndex == question.correctAnswerIndex

        // Record the answer for progress
        progressRepository.recordAnswer(
            questionId = question.id,
            selectedIndex = selectedIndex,
            isCorrect = isCorrect,
            mode = "learn"
        )

        return AnswerFeedback(
            question = question,
            selectedIndex = selectedIndex,
            isCorrect = isCorrect,
            correctIndex = question.correctAnswerIndex,
            explanation = question.explanation
        )
    }

    /**
     * Get a single question by ID.
     */
    fun getQuestionById(id: Int): Question? {
        return questionRepository.getQuestionById(id)
    }
}
