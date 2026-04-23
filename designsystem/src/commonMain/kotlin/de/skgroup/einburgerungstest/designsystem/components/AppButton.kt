package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface
import de.skgroup.einburgerungstest.designsystem.theme.ButtonShape
import androidx.compose.ui.tooling.preview.Preview

/**
 * Primary app button — filled green, rounded, full width.
 */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailingIcon: String? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
        )
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            trailingIcon?.let {
                Text(text = it, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

/**
 * Secondary outlined button.
 */
@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = ButtonShape,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

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
