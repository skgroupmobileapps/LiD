package de.skgroup.einburgerungstest.feature.exam

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun ExamIntroScreenPreview() {
    PreviewSurface {
        ExamIntroScreen(federalState = FederalState.BERLIN)
    }
}

@Preview
@Composable
private fun ExamIntroScreenDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ExamIntroScreen(federalState = FederalState.BERLIN)
    }
}

@Preview
@Composable
private fun ExamInfoItemPreview() {
    PreviewSurface {
        ExamInfoItem(
            icon = "📝",
            title = "33 questions",
            subtitle = "30 general questions plus 3 for Berlin"
        )
    }
}

@Preview
@Composable
private fun ExamInfoItemDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ExamInfoItem(
            icon = "📝",
            title = "33 questions",
            subtitle = "30 general questions plus 3 for Berlin"
        )
    }
}
