package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun AppButtonPreview() {
    PreviewSurface {
        AppButton(
            text = "Start learning",
            trailingIcon = "›",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun AppButtonDarkPreview() {
    PreviewSurface(darkTheme = true) {
        AppButton(
            text = "Start learning",
            trailingIcon = "›",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun AppOutlinedButtonPreview() {
    PreviewSurface {
        AppOutlinedButton(
            text = "Review mistakes",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun AppOutlinedButtonDarkPreview() {
    PreviewSurface(darkTheme = true) {
        AppOutlinedButton(
            text = "Review mistakes",
            onClick = {}
        )
    }
}
