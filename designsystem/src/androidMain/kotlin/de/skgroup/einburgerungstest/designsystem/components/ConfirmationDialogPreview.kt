package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun ConfirmationDialogPreview() {
    PreviewSurface {
        ConfirmationDialog(
            title = "Leave exam?",
            message = "Your progress for this attempt will be lost.",
            confirmText = "Leave",
            dismissText = "Stay",
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun ConfirmationDialogDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ConfirmationDialog(
            title = "Leave exam?",
            message = "Your progress for this attempt will be lost.",
            confirmText = "Leave",
            dismissText = "Stay",
            onConfirm = {},
            onDismiss = {}
        )
    }
}
