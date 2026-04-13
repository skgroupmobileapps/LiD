package de.skgroup.einburgerungstest.tracking

sealed class TrackingEvent(val name: String, val params: Map<String, Any> = emptyMap()) {
    data class ScreenView(val screenName: String) : TrackingEvent(
        name = "screen_view",
        params = mapOf("screen_name" to screenName)
    )

    data object OnboardingStarted : TrackingEvent(name = "onboarding_started")

    data class LanguageSelected(val language: String) : TrackingEvent(
        name = "language_selected",
        params = mapOf("language" to language)
    )

    data class FederalStateSelected(val federalState: String) : TrackingEvent(
        name = "federal_state_selected",
        params = mapOf("federal_state" to federalState)
    )

    data class OnboardingCompleted(
        val language: String,
        val federalState: String
    ) : TrackingEvent(
        name = "onboarding_completed",
        params = mapOf(
            "language" to language,
            "federal_state" to federalState
        )
    )

    data class TopicSelected(val topic: String) : TrackingEvent(
        name = "topic_selected",
        params = mapOf("topic" to topic)
    )

    data class QuestionAnswered(
        val questionId: Int,
        val isCorrect: Boolean,
        val mode: String,
        val topic: String
    ) : TrackingEvent(
        name = "question_answered",
        params = mapOf(
            "question_id" to questionId,
            "is_correct" to isCorrect,
            "mode" to mode,
            "topic" to topic
        )
    )

    data class BookmarkToggled(
        val questionId: Int,
        val isBookmarked: Boolean
    ) : TrackingEvent(
        name = "bookmark_toggled",
        params = mapOf(
            "question_id" to questionId,
            "is_bookmarked" to isBookmarked
        )
    )

    data class ExamStarted(val federalState: String) : TrackingEvent(
        name = "exam_started",
        params = mapOf("federal_state" to federalState)
    )

    data class ExamCompleted(
        val score: Int,
        val passed: Boolean,
        val timeSpentSeconds: Long,
        val federalState: String
    ) : TrackingEvent(
        name = "exam_completed",
        params = mapOf(
            "score" to score,
            "passed" to passed,
            "time_spent_seconds" to timeSpentSeconds,
            "federal_state" to federalState
        )
    )

    data class ExamAbandoned(
        val currentQuestionIndex: Int,
        val totalQuestions: Int,
        val timeSpentSeconds: Long
    ) : TrackingEvent(
        name = "exam_abandoned",
        params = mapOf(
            "current_question_index" to currentQuestionIndex,
            "total_questions" to totalQuestions,
            "time_spent_seconds" to timeSpentSeconds
        )
    )

    data class TimerExpired(
        val answeredQuestions: Int,
        val totalQuestions: Int
    ) : TrackingEvent(
        name = "timer_expired",
        params = mapOf(
            "answered_questions" to answeredQuestions,
            "total_questions" to totalQuestions
        )
    )

    data class ThemeChanged(val darkMode: Boolean) : TrackingEvent(
        name = "theme_changed",
        params = mapOf("dark_mode" to darkMode)
    )

    data class LanguageChanged(val newLanguage: String) : TrackingEvent(
        name = "language_changed",
        params = mapOf("new_language" to newLanguage)
    )

    data object SettingsReset : TrackingEvent(name = "settings_reset")

    data class ConsentChanged(val analyticsEnabled: Boolean) : TrackingEvent(
        name = "consent_changed",
        params = mapOf("analytics_enabled" to analyticsEnabled)
    )

    data class FeedbackDialogShown(val trigger: String) : TrackingEvent(
        name = "feedback_dialog_shown",
        params = mapOf("trigger" to trigger)
    )

    data class FeedbackSubmitted(val rating: Int, val trigger: String) : TrackingEvent(
        name = "feedback_submitted",
        params = mapOf("rating" to rating, "trigger" to trigger)
    )

    data class FeedbackDismissed(val trigger: String) : TrackingEvent(
        name = "feedback_dismissed",
        params = mapOf("trigger" to trigger)
    )

    data class StoreRedirectAccepted(val platform: String) : TrackingEvent(
        name = "store_redirect_accepted",
        params = mapOf("platform" to platform)
    )

    data object StoreRedirectDeclined : TrackingEvent(name = "store_redirect_declined")
}
