package de.skabs.skgroup.domain.usecase

import de.skabs.skgroup.core.model.Topic
import de.skabs.skgroup.core.model.UserProgress
import de.skabs.skgroup.core.util.Scoring
import de.skabs.skgroup.data.repository.BookmarkRepository
import de.skabs.skgroup.data.repository.ProgressRepository
import de.skabs.skgroup.data.repository.QuestionRepository

/**
 * Use case for computing overall and per-topic progress.
 */
class ProgressUseCase(
    private val progressRepository: ProgressRepository,
    private val questionRepository: QuestionRepository,
    private val bookmarkRepository: BookmarkRepository
) {
    /**
     * Get the complete user progress overview.
     */
    fun getUserProgress(): UserProgress {
        val totalAnswered = progressRepository.getTotalAnswered().toInt()
        val totalCorrect = progressRepository.getTotalCorrect().toInt()
        val distinctCorrect = progressRepository.getDistinctCorrectQuestionCount()
        val totalQuestions = questionRepository.getQuestionCount().toInt()
        val bookmarks = bookmarkRepository.getBookmarkCount().toInt()
        val streak = progressRepository.getDayStreak()

        val topicProgress = Topic.entries.associateWith { topic ->
            val questionsInTopic = questionRepository.getQuestionsByTopic(topic).size
            progressRepository.getTopicProgress(topic, questionsInTopic)
        }

        val overallPercent = if (totalQuestions > 0) {
            (distinctCorrect.toFloat() / totalQuestions.toFloat()) * 100f
        } else 0f

        return UserProgress(
            totalAnswered = totalAnswered,
            totalCorrect = totalCorrect,
            accuracy = Scoring.calculateAccuracy(totalCorrect, totalAnswered),
            bookmarkCount = bookmarks,
            dayStreak = streak,
            topicProgress = topicProgress,
            overallProgressPercent = overallPercent,
            totalQuestionsAvailable = totalQuestions
        )
    }
}
