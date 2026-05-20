package de.skgroup.einburgerungstest.feature.feedback

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun StoreRedirectDialogPreview() {
    PreviewSurface {
        StoreRedirectDialog(
            onRateInStore = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun StoreRedirectDialogDarkPreview() {
    PreviewSurface(darkTheme = true) {
        StoreRedirectDialog(
            onRateInStore = {},
            onDismiss = {}
        )
    }
}
