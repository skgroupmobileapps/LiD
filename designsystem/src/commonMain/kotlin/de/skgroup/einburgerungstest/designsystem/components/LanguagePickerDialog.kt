package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.core.model.Language
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview

/**
 * Reusable language picker dialog.
 *
 * Displays all available [Language] options with radio buttons.
 */
@Composable
fun LanguagePickerDialog(
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = stringResource(Res.string.profile_app_language),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(Language.entries.toList()) { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedLanguage = language }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RadioButton(
                            selected = selectedLanguage == language,
                            onClick = { selectedLanguage = language }
                        )
                        Column {
                            Text(
                                text = language.displayName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (selectedLanguage == language) FontWeight.SemiBold else FontWeight.Normal
                            )
                            Text(
                                text = when (language) {
                                    Language.GERMAN -> "German"
                                    Language.ENGLISH -> "English"
                                    Language.TURKISH -> "Turkish"
                                    Language.ARABIC -> "Arabic"
                                    Language.RUSSIAN -> "Russian"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onLanguageSelected(selectedLanguage)
                    onDismiss()
                }
            ) {
                Text(stringResource(Res.string.dialog_confirm), fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.dialog_cancel))
            }
        }
    )
}

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
