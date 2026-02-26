package de.skabs.skgroup.core.util

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
actual fun PlatformLocaleConfiguration(
    localeCode: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val currentConfiguration = LocalConfiguration.current
    
    val newConfiguration = remember(localeCode) {
        val locale = Locale(localeCode)
        Configuration(currentConfiguration).apply {
            setLocale(locale)
        }
    }
    
    val localizedContext = remember(newConfiguration) {
        context.createConfigurationContext(newConfiguration)
    }
    
    CompositionLocalProvider(
        LocalConfiguration provides newConfiguration,
        LocalContext provides localizedContext
    ) {
        content()
    }
}
