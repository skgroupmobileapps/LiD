package de.skgroup.einburgerungstest.data.repository

import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Language
import de.skgroup.einburgerungstest.core.model.ThemeMode
import de.skgroup.einburgerungstest.core.model.UserSettings
import de.skgroup.einburgerungstest.data.local.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository for app settings persistence.
 *
 * Exposes [settingsFlow] so the UI (e.g. theme) can reactively observe
 * changes like dark-mode toggle without restarting the app.
 */
class SettingsRepository(private val database: AppDatabase) {

    private val _settingsFlow = MutableStateFlow(UserSettings())
    val settingsFlow: StateFlow<UserSettings> = _settingsFlow.asStateFlow()

    init {
        // Load persisted settings into the flow on creation
        _settingsFlow.value = loadSettings()
    }

    companion object {
        private const val KEY_LANGUAGE = "language"
        private const val KEY_FEDERAL_STATE = "federal_state"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_ANALYTICS_ENABLED = "analytics_enabled"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_IS_GUEST = "is_guest"
        private const val KEY_LAST_FEEDBACK_TIMESTAMP = "last_feedback_timestamp"
        private const val KEY_LAST_FEEDBACK_RATING = "last_feedback_rating"
        private const val KEY_SUCCESSFUL_LEARN_SESSIONS = "successful_learn_sessions"
    }

    fun saveSettings(settings: UserSettings) {
        database.appDatabaseQueries.insertSetting(KEY_LANGUAGE, settings.language.name)
        database.appDatabaseQueries.insertSetting(KEY_FEDERAL_STATE, settings.federalState.name)
        database.appDatabaseQueries.insertSetting(KEY_THEME_MODE, settings.themeMode.name)
        database.appDatabaseQueries.insertSetting(KEY_ANALYTICS_ENABLED, settings.analyticsEnabled.toString())
        database.appDatabaseQueries.insertSetting(KEY_NOTIFICATIONS, settings.notificationsEnabled.toString())
        database.appDatabaseQueries.insertSetting(KEY_ONBOARDING_COMPLETED, settings.hasCompletedOnboarding.toString())
        database.appDatabaseQueries.insertSetting(KEY_IS_GUEST, settings.isGuest.toString())
        // Immediately push to flow so observers (theme, UI) react
        _settingsFlow.value = settings
    }

    fun loadSettings(): UserSettings {
        val language = getSetting(KEY_LANGUAGE)?.let {
            try { Language.valueOf(it) } catch (_: Exception) { null }
        } ?: Language.GERMAN

        val federalState = getSetting(KEY_FEDERAL_STATE)?.let {
            try { FederalState.valueOf(it) } catch (_: Exception) { null }
        } ?: FederalState.BERLIN

        // Migrate from old boolean dark_mode key if theme_mode not set yet
        val themeMode = getSetting(KEY_THEME_MODE)?.let {
            try { ThemeMode.valueOf(it) } catch (_: Exception) { null }
        } ?: run {
            val legacyDark = getSetting(KEY_DARK_MODE)?.toBooleanStrictOrNull()
            when (legacyDark) {
                true -> ThemeMode.DARK
                false -> ThemeMode.LIGHT
                null -> ThemeMode.SYSTEM
            }
        }

        val analyticsEnabled = getSetting(KEY_ANALYTICS_ENABLED)?.toBooleanStrictOrNull() ?: true
        val notifications = getSetting(KEY_NOTIFICATIONS)?.toBooleanStrictOrNull() ?: true
        val onboarding = getSetting(KEY_ONBOARDING_COMPLETED)?.toBooleanStrictOrNull() ?: false
        val isGuest = getSetting(KEY_IS_GUEST)?.toBooleanStrictOrNull() ?: true

        return UserSettings(
            language = language,
            federalState = federalState,
            themeMode = themeMode,
            analyticsEnabled = analyticsEnabled,
            notificationsEnabled = notifications,
            hasCompletedOnboarding = onboarding,
            isGuest = isGuest
        )
    }

    fun hasCompletedOnboarding(): Boolean {
        return getSetting(KEY_ONBOARDING_COMPLETED)?.toBooleanStrictOrNull() ?: false
    }

    fun setOnboardingCompleted() {
        database.appDatabaseQueries.insertSetting(KEY_ONBOARDING_COMPLETED, "true")
    }

    // --- Feedback cooldown helpers ---

    fun getLastFeedbackTimestamp(): Long {
        return getSetting(KEY_LAST_FEEDBACK_TIMESTAMP)?.toLongOrNull() ?: 0L
    }

    fun getLastFeedbackRating(): Int {
        return getSetting(KEY_LAST_FEEDBACK_RATING)?.toIntOrNull() ?: 0
    }

    fun setFeedbackSubmitted(rating: Int, timestampMs: Long) {
        database.appDatabaseQueries.insertSetting(KEY_LAST_FEEDBACK_TIMESTAMP, timestampMs.toString())
        database.appDatabaseQueries.insertSetting(KEY_LAST_FEEDBACK_RATING, rating.toString())
        database.appDatabaseQueries.insertSetting(KEY_SUCCESSFUL_LEARN_SESSIONS, "0")
    }

    fun getSuccessfulLearnSessionCount(): Int {
        return getSetting(KEY_SUCCESSFUL_LEARN_SESSIONS)?.toIntOrNull() ?: 0
    }

    fun incrementSuccessfulLearnSessions() {
        val current = getSuccessfulLearnSessionCount()
        database.appDatabaseQueries.insertSetting(KEY_SUCCESSFUL_LEARN_SESSIONS, (current + 1).toString())
    }

    private fun getSetting(key: String): String? {
        return database.appDatabaseQueries.getSetting(key).executeAsOneOrNull()
    }
}
