package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun QuickActionCardPrimaryPreview() {
    PreviewSurface {
        QuickActionCardPrimary(
            title = "Continue learning",
            subtitle = "Resume with the next unanswered question"
        )
    }
}

@Preview
@Composable
private fun QuickActionCardPrimaryDarkPreview() {
    PreviewSurface(darkTheme = true) {
        QuickActionCardPrimary(
            title = "Continue learning",
            subtitle = "Resume with the next unanswered question"
        )
    }
}

@Preview
@Composable
private fun QuickActionCardSecondaryPreview() {
    PreviewSurface {
        QuickActionCardSecondary(
            title = "Exam mode",
            subtitle = "Simulate the full test",
            icon = "🎓"
        )
    }
}

@Preview
@Composable
private fun QuickActionCardSecondaryDarkPreview() {
    PreviewSurface(darkTheme = true) {
        QuickActionCardSecondary(
            title = "Exam mode",
            subtitle = "Simulate the full test",
            icon = "🎓"
        )
    }
}
