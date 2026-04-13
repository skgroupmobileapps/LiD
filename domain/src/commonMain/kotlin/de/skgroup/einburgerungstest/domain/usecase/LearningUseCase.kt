package de.skgroup.einburgerungstest.domain.usecase

import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Question
import de.skgroup.einburgerungstest.core.model.Topic
import de.skgroup.einburgerungstest.data.repository.ProgressRepository
import de.skgroup.einburgerungstest.data.repository.QuestionRepository

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
     * Get all questions for a specific topic (unfiltered by federal state).
     */
    fun getQuestionsForTopic(topic: Topic): List<Question> {
        return questionRepository.getQuestionsByTopic(topic)
    }

    /**
     * Get questions for a specific topic, filtered for the user's federal state.
     * For FEDERAL_STATE topic, returns only the 10 questions for the user's Land.
     * For other topics, returns all general questions in that topic.
     */
    fun getQuestionsForTopicAndState(topic: Topic, federalState: FederalState): List<Question> {
        return questionRepository.getQuestionsByTopicForUser(topic, federalState)
    }

    /**
     * Get all candidate questions (300 general + 10 for their state).
     */
    fun getCandidateQuestions(federalState: FederalState): List<Question> {
        return questionRepository.getCandidateQuestions(federalState)
    }

    /**
     * Get all general questions (unfiltered - for backwards compatibility).
     * @deprecated Use getCandidateQuestions(federalState) instead for proper Land filtering.
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
