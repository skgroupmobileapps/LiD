package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.designsystem.theme.ErrorRedDark
import de.skgroup.einburgerungstest.designsystem.theme.ErrorRedLight
import de.skgroup.einburgerungstest.designsystem.theme.ErrorRedSurfaceDark
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface
import de.skgroup.einburgerungstest.designsystem.theme.PrimaryGreen
import de.skgroup.einburgerungstest.designsystem.theme.SuccessGreenLight
import de.skgroup.einburgerungstest.designsystem.theme.SuccessGreenSurfaceDark
import de.skgroup.einburgerungstest.designsystem.theme.SuccessGreenTextDark
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * Feedback display shown after answering a question in learn mode.
 * Shows "Correct!" or "Wrong!" with explanation text.
 */
@Composable
fun AnswerFeedback(
    isCorrect: Boolean,
    explanation: String,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isCorrect) {
        if (isDark) SuccessGreenSurfaceDark else SuccessGreenLight
    } else {
        if (isDark) ErrorRedSurfaceDark else ErrorRedLight
    }
    // Title text: use lighter variants in dark mode for sufficient contrast on dark surfaces.
    // Light mode:
    //   PrimaryGreen (#1B5E3B) on SuccessGreenLight (#E6F7ED) = 6.6:1 ✅ (was SuccessGreen = 2.76:1 ❌)
    //   ErrorRedDark (#B71C1C) on ErrorRedLight (#FFF0F0) = 5.6:1 ✅ (was ErrorRed = 3.56:1 marginal)
    // Dark mode:
    //   SuccessGreenTextDark (#68D391) on SuccessGreenSurfaceDark (#1B3A2E) = ~8.9:1 ✅
    //   #FF8A80 on ErrorRedSurfaceDark (#3D1515) = ~7.7:1 ✅
    val textColor = if (isCorrect) {
        if (isDark) SuccessGreenTextDark else PrimaryGreen
    } else {
        if (isDark) Color(0xFFFF8A80) else ErrorRedDark
    }
    // Explanation text must contrast with our custom bg, not the generic surface
    val explanationTextColor = if (isDark) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface
    val title = if (isCorrect) "✅ Correct!" else "❌ Wrong!"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            if (!isCorrect) {
                Text(
                    text = explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = explanationTextColor,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AnswerFeedbackCorrectPreview() {
    PreviewSurface {
        AnswerFeedback(
            isCorrect = true,
            explanation = "This answer matches the constitutional principle being tested."
        )
    }
}

@Preview
@Composable
private fun AnswerFeedbackCorrectDarkPreview() {
    PreviewSurface(darkTheme = true) {
        AnswerFeedback(
            isCorrect = true,
            explanation = "This answer matches the constitutional principle being tested."
        )
    }
}

@Preview
@Composable
private fun AnswerFeedbackWrongPreview() {
    PreviewSurface {
        AnswerFeedback(
            isCorrect = false,
            explanation = "The Basic Law protects freedom of expression, but it does not remove all legal limits."
        )
    }
}

@Preview
@Composable
private fun AnswerFeedbackWrongDarkPreview() {
    PreviewSurface(darkTheme = true) {
        AnswerFeedback(
            isCorrect = false,
            explanation = "The Basic Law protects freedom of expression, but it does not remove all legal limits."
        )
    }
}
