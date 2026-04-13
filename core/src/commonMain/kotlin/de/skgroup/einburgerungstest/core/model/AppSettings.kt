package de.skgroup.einburgerungstest.core.model

import kotlinx.serialization.Serializable

/**
 * Supported app languages.
 */
@Serializable
enum class Language(val displayName: String, val code: String) {
    GERMAN("Deutsch", "de"),
    ENGLISH("English", "en"),
    TURKISH("Türkçe", "tr"),
    ARABIC("العربية", "ar"),
    RUSSIAN("Русский", "ru");
}

/**
 * App-wide user settings, persisted locally.
 */
@Serializable
data class UserSettings(
    val language: Language = Language.GERMAN,
    val federalState: FederalState = FederalState.BERLIN,
    val darkMode: Boolean = false,
    val analyticsEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val hasCompletedOnboarding: Boolean = false,
    val isGuest: Boolean = true
)
