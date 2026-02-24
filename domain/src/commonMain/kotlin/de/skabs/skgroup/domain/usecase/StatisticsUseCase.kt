package de.skabs.skgroup.domain.usecase

import de.skabs.skgroup.core.model.Topic
import de.skabs.skgroup.core.model.TopicProgress
import de.skabs.skgroup.core.util.Scoring
import de.skabs.skgroup.data.repository.ExamRepository
import de.skabs.skgroup.data.repository.ProgressRepository
import de.skabs.skgroup.data.repository.QuestionRepository

/**
 * Use case for computing statistics and analytics.
 */
class StatisticsUseCase(
    private val progressRepository: ProgressRepository,
    private val examRepository: ExamRepository,
    private val questionRepository: QuestionRepository
) {
    /**
     * Get the overall accuracy across all answered questions.
     */
    fun getOverallAccuracy(): Float {
        val total = progressRepository.getTotalAnswered()
        val correct = progressRepository.getTotalCorrect()
        return Scoring.calculateAccuracy(correct.toInt(), total.toInt())
    }

    /**
     * Get the current day streak.
     */
    fun getDayStreak(): Int {
        return progressRepository.getDayStreak()
    }

    /**
     * Get progress for each topic, identifying weak areas.
     */
    fun getTopicProgressList(): List<TopicProgress> {
        return Topic.entries.map { topic ->
            val questionsInTopic = questionRepository.getQuestionsByTopic(topic).size
            progressRepository.getTopicProgress(topic, questionsInTopic)
        }
    }

    /**
     * Get the weakest topics (lowest accuracy), useful for focused study.
     */
    fun getWeakTopics(limit: Int = 3): List<TopicProgress> {
        return getTopicProgressList()
            .filter { it.totalAnswered > 0 }
            .sortedBy { it.accuracyPercent }
            .take(limit)
    }

    /**
     * Get exam statistics.
     */
    fun getExamStats(): ExamStats {
        val count = examRepository.getExamCount()
        val passed = examRepository.getPassedExamCount()
        val avg = examRepository.getAverageScore() ?: 0.0
        return ExamStats(
            totalAttempts = count.toInt(),
            totalPassed = passed.toInt(),
            averageScore = avg.toFloat()
        )
    }
}

data class ExamStats(
    val totalAttempts: Int = 0,
    val totalPassed: Int = 0,
    val averageScore: Float = 0f
)
