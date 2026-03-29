package de.skabs.skgroup.designsystem.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import de.skabs.skgroup.designsystem.theme.PreviewSurface
import androidx.compose.ui.tooling.preview.Preview

/**
 * Reusable confirmation dialog following the app design system.
 *
 * Used for destructive actions such as leaving exam mode or resetting statistics.
 */
@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(confirmText, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large
    )
}

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
