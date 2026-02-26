package de.skabs.skgroup.analytics

/**
 * Sealed class representing all trackable analytics events.
 */
sealed class AnalyticsEvent(val name: String, val params: Map<String, Any> = emptyMap()) {
    
    /**
     * Screen view event.
     */
    data class ScreenView(val screenName: String) : AnalyticsEvent(
        name = "screen_view",
        params = mapOf("screen_name" to screenName)
    )
    
    /**
     * User completed onboarding.
     */
    data class OnboardingCompleted(
        val language: String,
        val federalState: String
    ) : AnalyticsEvent(
        name = "onboarding_completed",
        params = mapOf(
            "language" to language,
            "federal_state" to federalState
        )
    )
    
    /**
     * User started an exam.
     */
    data class ExamStarted(val federalState: String) : AnalyticsEvent(
        name = "exam_started",
        params = mapOf("federal_state" to federalState)
    )
    
    /**
     * User completed an exam.
     */
    data class ExamCompleted(
        val score: Int,
        val passed: Boolean,
        val timeSpentSeconds: Long
    ) : AnalyticsEvent(
        name = "exam_completed",
        params = mapOf(
            "score" to score,
            "passed" to passed,
            "time_spent_seconds" to timeSpentSeconds
        )
    )
    
    /**
     * User answered a question.
     */
    data class QuestionAnswered(
        val questionId: Int,
        val isCorrect: Boolean,
        val mode: String // "learn" or "exam"
    ) : AnalyticsEvent(
        name = "question_answered",
        params = mapOf(
            "question_id" to questionId,
            "is_correct" to isCorrect,
            "mode" to mode
        )
    )
    
    /**
     * User toggled bookmark.
     */
    data class BookmarkToggled(
        val questionId: Int,
        val isBookmarked: Boolean
    ) : AnalyticsEvent(
        name = "bookmark_toggled",
        params = mapOf(
            "question_id" to questionId,
            "is_bookmarked" to isBookmarked
        )
    )
    
    /**
     * User changed theme.
     */
    data class ThemeChanged(val darkMode: Boolean) : AnalyticsEvent(
        name = "theme_changed",
        params = mapOf("dark_mode" to darkMode)
    )
    
    /**
     * User changed app language.
     */
    data class LanguageChanged(val newLanguage: String) : AnalyticsEvent(
        name = "language_changed",
        params = mapOf("new_language" to newLanguage)
    )
    
    /**
     * User reset statistics.
     */
    data object SettingsReset : AnalyticsEvent(name = "settings_reset")
}
