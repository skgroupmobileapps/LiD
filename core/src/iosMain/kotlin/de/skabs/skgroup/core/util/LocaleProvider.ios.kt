package de.skabs.skgroup.core.util

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformLocaleConfiguration(
    localeCode: String,
    content: @Composable () -> Unit
) {
    // On iOS, compose resources will use the platform locale settings
    // For now, just render the content directly
    // Future: Use NSUserDefaults or bundle language settings
    content()
}
