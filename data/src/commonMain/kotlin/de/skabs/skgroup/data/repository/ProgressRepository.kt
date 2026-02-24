package de.skabs.skgroup.data.repository

import de.skabs.skgroup.core.model.Topic
import de.skabs.skgroup.core.model.TopicProgress
import de.skabs.skgroup.core.model.UserProgress
import de.skabs.skgroup.data.local.AppDatabase
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Repository for tracking learning progress.
 */
class ProgressRepository(private val database: AppDatabase) {

    fun recordAnswer(questionId: Int, selectedIndex: Int, isCorrect: Boolean, mode: String = "learn") {
        val now = Clock.System.now().toEpochMilliseconds()
        database.appDatabaseQueries.insertAnswer(
            questionId = questionId.toLong(),
            selectedIndex = selectedIndex.toLong(),
            isCorrect = if (isCorrect) 1L else 0L,
            timestampMs = now,
            mode = mode
        )
    }

    fun getTotalAnswered(): Long {
        return database.appDatabaseQueries.getTotalAnswerCount().executeAsOne()
    }

    fun getTotalCorrect(): Long {
        return database.appDatabaseQueries.getCorrectAnswerCount().executeAsOne()
    }

    fun getDistinctCorrectQuestionCount(): Int {
        return database.appDatabaseQueries.getDistinctCorrectQuestionIds().executeAsList().size
    }

    fun getTopicProgress(topic: Topic, totalQuestionsInTopic: Int): TopicProgress {
        val correct = database.appDatabaseQueries.getCorrectCountByTopic(topic.name).executeAsOne()
        val total = database.appDatabaseQueries.getTotalCountByTopic(topic.name).executeAsOne()
        return TopicProgress(
            topic = topic,
            totalQuestions = totalQuestionsInTopic,
            answeredCorrectly = correct.toInt(),
            answeredWrong = (total - correct).toInt(),
            totalAnswered = total.toInt()
        )
    }

    /**
     * Calculate the current day streak.
     * A streak counts consecutive days (up to today) where the user answered at least one question.
     */
    fun getDayStreak(): Int {
        val days = database.appDatabaseQueries.getAnswersByDate().executeAsList()
        if (days.isEmpty()) return 0

        val today = Clock.System.now().toEpochMilliseconds() / 86400000L
        var streak = 0
        var expectedDay = today

        for (entry in days) {
            val day = entry ?: continue
            if (day == expectedDay || day == expectedDay - 1) {
                streak++
                expectedDay = day - 1
            } else {
                break
            }
        }
        return streak
    }
}
