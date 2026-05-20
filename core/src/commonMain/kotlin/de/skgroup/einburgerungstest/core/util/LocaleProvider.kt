package de.skgroup.einburgerungstest.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import de.skgroup.einburgerungstest.core.model.Language

/**
 * CompositionLocal for the current app language.
 */
val LocalAppLanguage = compositionLocalOf { Language.GERMAN }

/**
 * Get the locale code for a Language.
 */
fun Language.toLocaleCode(): String = when (this) {
    Language.GERMAN -> "de"
    Language.ENGLISH -> "en"
    Language.TURKISH -> "tr"
    Language.ARABIC -> "ar"
    Language.RUSSIAN -> "ru"
}

/**
 * Provider that sets up app localization based on user settings.
 */
@Composable
fun AppLocaleProvider(
    language: Language,
    content: @Composable () -> Unit
) {
    val localeCode = remember(language) { language.code }
    
    // Provide new locals — CompositionLocalProvider propagates them to all consumers via
    // normal recomposition, so strings re-resolve without needing key() to destroy/recreate
    // the subtree (which would wipe NavHost and onboarding step state).
    CompositionLocalProvider(
        LocalAppLanguage provides language,
        LocalAppLocale provides localeCode,
        content = content
    )
}

/**
 * Platform-specific app locale configuration for compose resources.
 */
expect object LocalAppLocale {
    val current: String
        @Composable get

    @Composable
    infix fun provides(value: String?): ProvidedValue<*>
}
