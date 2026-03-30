package de.skabs.skgroup.domain.usecase

import de.skabs.skgroup.data.repository.SettingsRepository
import kotlinx.datetime.Clock

class FeedbackUseCase(
    private val settingsRepository: SettingsRepository,
    private val statisticsUseCase: StatisticsUseCase
) {
    companion object {
        private const val POSITIVE_COOLDOWN_MS = 90L * 24 * 60 * 60 * 1000  // 3 months
        private const val NEGATIVE_COOLDOWN_MS = 30L * 24 * 60 * 60 * 1000  // 1 month
        private const val MIN_ANSWERS_FOR_ENGAGEMENT = 50
        private const val MIN_DAYS_FOR_ENGAGEMENT = 3
        private const val LEARN_SESSIONS_FOR_TRIGGER = 3
    }

    fun shouldShowFeedback(): Boolean {
        if (!settingsRepository.hasCompletedOnboarding()) return false
        if (!hasMinimumEngagement()) return false
        if (isCooldownActive()) return false
        return true
    }

    fun shouldShowFeedbackAfterExam(passed: Boolean): Boolean {
        return passed && shouldShowFeedback()
    }

    fun shouldShowFeedbackAfterLearning(): Boolean {
        val sessions = settingsRepository.getSuccessfulLearnSessionCount()
        return sessions >= LEARN_SESSIONS_FOR_TRIGGER && shouldShowFeedback()
    }

    fun recordSuccessfulLearnSession() {
        settingsRepository.incrementSuccessfulLearnSessions()
    }

    fun recordFeedbackSubmitted(rating: Int) {
        val now = Clock.System.now().toEpochMilliseconds()
        settingsRepository.setFeedbackSubmitted(rating, now)
    }

    fun recordFeedbackDismissed() {
        // Treat dismiss as a rating of 0 — applies 1-month cooldown
        val now = Clock.System.now().toEpochMilliseconds()
        settingsRepository.setFeedbackSubmitted(0, now)
    }

    fun isPositiveRating(rating: Int): Boolean = rating >= 4

    private fun hasMinimumEngagement(): Boolean {
        val totalAnswered = statisticsUseCase.getTotalAnswerCount()
        if (totalAnswered >= MIN_ANSWERS_FOR_ENGAGEMENT) return true
        val activityDays = statisticsUseCase.getActivityDayCount()
        return activityDays >= MIN_DAYS_FOR_ENGAGEMENT
    }

    private fun isCooldownActive(): Boolean {
        val lastTimestamp = settingsRepository.getLastFeedbackTimestamp()
        if (lastTimestamp == 0L) return false

        val now = Clock.System.now().toEpochMilliseconds()
        val elapsed = now - lastTimestamp
        val lastRating = settingsRepository.getLastFeedbackRating()

        val cooldownMs = if (isPositiveRating(lastRating)) POSITIVE_COOLDOWN_MS else NEGATIVE_COOLDOWN_MS
        return elapsed < cooldownMs
    }
}
