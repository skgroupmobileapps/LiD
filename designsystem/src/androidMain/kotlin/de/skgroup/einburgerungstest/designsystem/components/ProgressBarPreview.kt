package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun AppProgressBarPreview() {
    PreviewSurface {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppProgressBar(progress = 0.25f)
            AppProgressBar(progress = 0.68f)
            AppProgressBar(progress = 1f)
        }
    }
}

@Preview
@Composable
private fun AppProgressBarDarkPreview() {
    PreviewSurface(darkTheme = true) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppProgressBar(progress = 0.25f)
            AppProgressBar(progress = 0.68f)
            AppProgressBar(progress = 1f)
        }
    }
}
