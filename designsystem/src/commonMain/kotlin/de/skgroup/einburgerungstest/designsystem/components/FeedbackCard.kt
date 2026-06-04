package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.designsystem.theme.ErrorRed
import de.skgroup.einburgerungstest.designsystem.theme.ErrorRedDark
import de.skgroup.einburgerungstest.designsystem.theme.ErrorRedLight
import de.skgroup.einburgerungstest.designsystem.theme.ErrorRedSurfaceDark
import de.skgroup.einburgerungstest.designsystem.theme.PrimaryGreen
import de.skgroup.einburgerungstest.designsystem.theme.SuccessGreenLight
import de.skgroup.einburgerungstest.designsystem.theme.SuccessGreenSurfaceDark
import de.skgroup.einburgerungstest.designsystem.theme.SuccessGreenTextDark

@Composable
fun FeedbackCard(
    isPositive: Boolean,
    title: String,
    modifier: Modifier = Modifier,
    leadingEmoji: String? = null,
    showBorder: Boolean = false,
    content: (@Composable ColumnScope.(contentColor: Color) -> Unit)? = null
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val bgColor = if (isPositive) {
        if (isDark) SuccessGreenSurfaceDark else SuccessGreenLight
    } else {
        if (isDark) ErrorRedSurfaceDark else ErrorRedLight
    }
    val titleColor = if (isPositive) {
        if (isDark) SuccessGreenTextDark else PrimaryGreen
    } else {
        if (isDark) MaterialTheme.colorScheme.onErrorContainer else ErrorRedDark
    }
    val contentColor = if (!isPositive && isDark) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val border = if (showBorder) BorderStroke(1.dp, ErrorRed.copy(alpha = 0.3f)) else null

    androidx.compose.material3.Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = border
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
                .padding(16.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingEmoji != null) {
                    Text(leadingEmoji, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor
                )
            }

            content?.let {
                Spacer(Modifier.height(12.dp))
                it(contentColor)
            }
        }
    }
}
