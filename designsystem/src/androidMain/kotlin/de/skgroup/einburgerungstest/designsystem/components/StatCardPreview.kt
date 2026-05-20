package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun StatCardPreview() {
    PreviewSurface {
        StatCard(
            icon = "🔥",
            value = "12",
            label = "Day streak"
        )
    }
}

@Preview
@Composable
private fun StatCardDarkPreview() {
    PreviewSurface(darkTheme = true) {
        StatCard(
            icon = "🔥",
            value = "12",
            label = "Day streak"
        )
    }
}
