package de.skabs.skgroup.domain.usecase

import de.skabs.skgroup.core.model.FederalState
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
     * Get the set of IDs of all questions the user has answered.
     */
    fun getAnsweredQuestionIds(): Set<Int> {
        return progressRepository.getAnsweredQuestionIds()
    }

    /**
     * Get progress for each topic (unfiltered - includes all 460 questions).
     * @deprecated Use getTopicProgressListForUser(federalState) for correct Land-based filtering.
     */
    fun getTopicProgressList(): List<TopicProgress> {
        return Topic.entries.map { topic ->
            val questionsInTopic = questionRepository.getQuestionsByTopic(topic).size
            progressRepository.getTopicProgress(topic, questionsInTopic)
        }
    }

    /**
     * Get progress for each topic, filtered for the user's federal state.
     * For FEDERAL_STATE topic, counts only the 10 questions for the user's Land.
     * For other topics, counts all general questions in that topic.
     */
    fun getTopicProgressListForUser(federalState: FederalState): List<TopicProgress> {
        return Topic.entries.map { topic ->
            val questionsInTopic = questionRepository.getQuestionsByTopicForUser(topic, federalState).size
            progressRepository.getTopicProgress(topic, questionsInTopic)
        }
    }

    /**
     * Get the weakest topics (lowest accuracy), useful for focused study.
     * @deprecated Use getWeakTopicsForUser(federalState, limit) for correct Land-based filtering.
     */
    fun getWeakTopics(limit: Int = 3): List<TopicProgress> {
        return getTopicProgressList()
            .filter { it.totalAnswered > 0 }
            .sortedBy { it.accuracyPercent }
            .take(limit)
    }

    /**
     * Get the weakest topics (lowest accuracy) for the user's federal state.
     */
    fun getWeakTopicsForUser(federalState: FederalState, limit: Int = 3): List<TopicProgress> {
        return getTopicProgressListForUser(federalState)
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
