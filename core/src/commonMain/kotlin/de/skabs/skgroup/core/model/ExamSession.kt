package de.skabs.skgroup.core.model

import kotlinx.serialization.Serializable

/**
 * Represents an active exam session with 33 questions.
 */
@Serializable
data class ExamSession(
    val id: Long = 0,
    val questions: List<Question>,
    val answers: MutableMap<Int, Int> = mutableMapOf(), // questionId -> selectedAnswerIndex
    val startTimeMs: Long,
    val timeLimitMs: Long = 60 * 60 * 1000L, // 60 minutes
    val federalState: FederalState
) {
    val totalQuestions: Int get() = questions.size
    val answeredCount: Int get() = answers.size
    val isComplete: Boolean get() = answeredCount >= totalQuestions
    val remainingQuestions: Int get() = totalQuestions - answeredCount
}

/**
 * A single wrong answer for review after an exam.
 */
@Serializable
data class WrongAnswer(
    val question: Question,
    val selectedAnswerIndex: Int,
    val correctAnswerIndex: Int
)

/**
 * Result of a completed exam session.
 */
@Serializable
data class ExamResult(
    val sessionId: Long = 0,
    val totalQuestions: Int,     // 33
    val correctCount: Int,
    val wrongCount: Int,
    val passed: Boolean,         // >= 17 correct (51%)
    val scorePercent: Float,
    val timeSpentMs: Long,
    val wrongAnswers: List<WrongAnswer>,
    val federalState: FederalState,
    val timestampMs: Long
) {
    companion object {
        const val PASS_THRESHOLD = 17
        const val TOTAL_EXAM_QUESTIONS = 33
        const val GENERAL_QUESTIONS_COUNT = 30
        const val STATE_QUESTIONS_COUNT = 3
    }
}
