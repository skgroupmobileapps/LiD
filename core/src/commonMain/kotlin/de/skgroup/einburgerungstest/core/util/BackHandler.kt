package de.skgroup.einburgerungstest.core.util

import androidx.compose.runtime.Composable

/**
 * Handles back button/gesture press across platforms.
 *
 * @param enabled Whether the back handler is enabled
 * @param onBack Callback invoked when back is pressed
 */
@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)
