package de.skgroup.einburgerungstest.feature.feedback

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun FeedbackDialogPreview() {
    PreviewSurface {
        FeedbackDialog(
            onSubmit = { _, _ -> },
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun FeedbackDialogDarkPreview() {
    PreviewSurface(darkTheme = true) {
        FeedbackDialog(
            onSubmit = { _, _ -> },
            onDismiss = {}
        )
    }
}
