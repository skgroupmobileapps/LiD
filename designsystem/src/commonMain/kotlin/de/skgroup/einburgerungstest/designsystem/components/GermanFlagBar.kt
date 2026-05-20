package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.designsystem.theme.GermanBlack
import de.skgroup.einburgerungstest.designsystem.theme.GermanRed
import de.skgroup.einburgerungstest.designsystem.theme.GermanGold

/**
 * German flag bar — thin horizontal black-red-gold stripe.
 * Used as a decorative element at the top of the Home screen.
 */
@Composable
fun GermanFlagBar(
    modifier: Modifier = Modifier,
    height: Int = 4
) {
    Row(modifier = modifier.fillMaxWidth().height(height.dp)) {
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(GermanBlack))
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(GermanRed))
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(GermanGold))
    }
}


