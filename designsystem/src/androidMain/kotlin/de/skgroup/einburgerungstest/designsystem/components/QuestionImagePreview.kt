package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun QuestionImagePreview() {
    PreviewSurface {
        QuestionImage(imageName = "q_21.png")
    }
}

@Preview
@Composable
private fun QuestionImageDarkPreview() {
    PreviewSurface(darkTheme = true) {
        QuestionImage(imageName = "q_21.png")
    }
}

@Preview
@Composable
private fun QuestionImageNullPreview() {
    PreviewSurface {
        QuestionImage(imageName = null)
    }
}
