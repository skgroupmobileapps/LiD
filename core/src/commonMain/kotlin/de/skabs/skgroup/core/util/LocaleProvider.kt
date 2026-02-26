package de.skabs.skgroup.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import de.skabs.skgroup.core.model.Language

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
    val localeCode = remember(language) { language.toLocaleCode() }
    
    CompositionLocalProvider(
        LocalAppLanguage provides language
    ) {
        // Configure the locale for compose resources
        PlatformLocaleConfiguration(localeCode) {
            content()
        }
    }
}

/**
 * Platform-specific locale configuration.
 */
@Composable
expect fun PlatformLocaleConfiguration(
    localeCode: String,
    content: @Composable () -> Unit
)
