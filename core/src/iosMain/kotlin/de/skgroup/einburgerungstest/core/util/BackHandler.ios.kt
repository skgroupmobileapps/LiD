package de.skgroup.einburgerungstest.core.util

import androidx.compose.runtime.Composable

/**
 * iOS doesn't have a system-level back gesture like Android.
 * This is a no-op implementation.
 */
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op on iOS - there's no system back gesture to intercept
}
