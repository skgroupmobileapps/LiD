package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun GermanFlagBarPreview() {
    PreviewSurface {
        GermanFlagBar(height = 8)
    }
}

@Preview
@Composable
private fun GermanFlagBarDarkPreview() {
    PreviewSurface(darkTheme = true) {
        GermanFlagBar(height = 8)
    }
}
