package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.core.model.Language
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun LanguagePickerDialogPreview() {
    PreviewSurface {
        LanguagePickerDialog(
            currentLanguage = Language.ENGLISH,
            onLanguageSelected = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun LanguagePickerDialogDarkPreview() {
    PreviewSurface(darkTheme = true) {
        LanguagePickerDialog(
            currentLanguage = Language.ENGLISH,
            onLanguageSelected = {},
            onDismiss = {}
        )
    }
}
