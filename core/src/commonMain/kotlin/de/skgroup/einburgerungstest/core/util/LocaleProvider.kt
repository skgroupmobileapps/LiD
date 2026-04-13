package de.skgroup.einburgerungstest.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.key
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
    
    CompositionLocalProvider(
        LocalAppLanguage provides language,
        LocalAppLocale provides localeCode
    ) {
        // Recreate subtree on locale change so string resources are re-resolved.
        key(localeCode) {
            content()
        }
    }
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
