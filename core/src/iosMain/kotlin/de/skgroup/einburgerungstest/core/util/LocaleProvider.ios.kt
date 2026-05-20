package de.skgroup.einburgerungstest.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import platform.Foundation.NSUserDefaults

actual object LocalAppLocale {
    private const val LANGUAGE_KEY = "AppleLanguages"
    private val defaultLocale = NSUserDefaults.standardUserDefaults
        .stringArrayForKey(LANGUAGE_KEY)
        ?.firstOrNull() as? String
        ?: "de"
    private val localAppLocale = compositionLocalOf { defaultLocale }

    actual val current: String
        @Composable get() = localAppLocale.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newLocale = value ?: defaultLocale

        if (value == null) {
            NSUserDefaults.standardUserDefaults.removeObjectForKey(LANGUAGE_KEY)
        } else {
            NSUserDefaults.standardUserDefaults.setObject(arrayListOf(newLocale), LANGUAGE_KEY)
        }

        return localAppLocale.provides(newLocale)
    }
}
