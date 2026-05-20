package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface
import kmpexam.resources.generated.resources.Res
import kmpexam.resources.generated.resources.q_21

@Preview
@Composable
private fun FullscreenImageViewerPreview() {
    PreviewSurface {
        FullscreenImageViewer(
            drawableRes = Res.drawable.q_21,
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun FullscreenImageViewerDarkPreview() {
    PreviewSurface(darkTheme = true) {
        FullscreenImageViewer(
            drawableRes = Res.drawable.q_21,
            onDismiss = {}
        )
    }
}
