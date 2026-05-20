package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface
import de.skgroup.einburgerungstest.designsystem.theme.PrimaryGreen

@Preview
@Composable
private fun TopicCardPreview() {
    PreviewSurface {
        TopicCard(
            title = "Democracy & State",
            description = "German political system, constitution, and democratic principles",
            icon = "🏛",
            accentColor = PrimaryGreen,
            progress = 0.42f,
            answeredCount = 18,
            totalCount = 43
        )
    }
}

@Preview
@Composable
private fun TopicCardDarkPreview() {
    PreviewSurface(darkTheme = true) {
        TopicCard(
            title = "Democracy & State",
            description = "German political system, constitution, and democratic principles",
            icon = "🏛",
            accentColor = PrimaryGreen,
            progress = 0.42f,
            answeredCount = 18,
            totalCount = 43
        )
    }
}
