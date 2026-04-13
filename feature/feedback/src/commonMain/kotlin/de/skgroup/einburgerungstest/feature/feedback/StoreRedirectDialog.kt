package de.skgroup.einburgerungstest.feature.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.designsystem.theme.PrimaryGreen
import de.skgroup.einburgerungstest.designsystem.theme.SuccessGreenLight
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun StoreRedirectDialog(
    onRateInStore: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = SuccessGreenLight
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("⭐", style = MaterialTheme.typography.headlineMedium)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(Res.string.feedback_store_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(
                text = stringResource(Res.string.feedback_store_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = onRateInStore,
                colors = ButtonDefaults.textButtonColors(contentColor = PrimaryGreen)
            ) {
                Text(
                    stringResource(Res.string.feedback_rate_store),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.feedback_not_now))
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large
    )
}
