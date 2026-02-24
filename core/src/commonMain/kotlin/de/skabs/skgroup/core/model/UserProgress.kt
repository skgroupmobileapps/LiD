package de.skabs.skgroup.core.model

import kotlinx.serialization.Serializable

/**
 * Overall progress across all questions and topics.
 */
@Serializable
data class UserProgress(
    val totalAnswered: Int = 0,
    val totalCorrect: Int = 0,
    val accuracy: Float = 0f,
    val bookmarkCount: Int = 0,
    val dayStreak: Int = 0,
    val topicProgress: Map<Topic, TopicProgress> = emptyMap(),
    val overallProgressPercent: Float = 0f,
    val totalQuestionsAvailable: Int = 310
)

/**
 * Progress within a single topic.
 */
@Serializable
data class TopicProgress(
    val topic: Topic,
    val totalQuestions: Int,
    val answeredCorrectly: Int = 0,
    val answeredWrong: Int = 0,
    val totalAnswered: Int = 0
) {
    val progressPercent: Float
        get() = if (totalQuestions > 0) (answeredCorrectly.toFloat() / totalQuestions) * 100f else 0f

    val accuracyPercent: Float
        get() = if (totalAnswered > 0) (answeredCorrectly.toFloat() / totalAnswered) * 100f else 0f
}

/**
 * Exam history entry for the profile screen.
 */
@Serializable
data class ExamHistoryEntry(
    val id: Long = 0,
    val totalQuestions: Int,
    val correctCount: Int,
    val passed: Boolean,
    val scorePercent: Float,
    val timestampMs: Long,
    val federalState: FederalState
)
