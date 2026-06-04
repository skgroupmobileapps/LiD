package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

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
    val title = if (isCorrect) "Correct!" else "Wrong!"

    FeedbackCard(
        isPositive = isCorrect,
        title = title,
        leadingEmoji = if (isCorrect) "✅" else "❌",
        modifier = modifier
    ) { contentColor ->
        if (!isCorrect) {
            Column {
                Text(
                    text = explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

