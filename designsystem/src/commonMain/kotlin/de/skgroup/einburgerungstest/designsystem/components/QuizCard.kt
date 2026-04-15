package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme
import de.skgroup.einburgerungstest.designsystem.theme.*
import androidx.compose.ui.tooling.preview.Preview

/**
 * State of a quiz answer card.
 */
enum class QuizCardState {
    DEFAULT,    // Unselected, neutral
    SELECTED,   // User selected this answer (exam mode - green highlight, no right/wrong)
    CORRECT,    // Revealed as correct (learn mode)
    WRONG,      // Revealed as wrong (learn mode)
    DISABLED    // Cannot be selected
}

/**
 * Multiple-choice answer card used in both learn and exam modes.
 *
 * Matches the mockup: rounded card with letter label circle on the left,
 * answer text center-left, with selection/feedback states.
 */
@Composable
fun QuizCard(
    label: String,
    text: String,
    state: QuizCardState = QuizCardState.DEFAULT,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            QuizCardState.DEFAULT -> MaterialTheme.colorScheme.surface
            QuizCardState.SELECTED -> if (isDark) PrimaryGreenSurfaceDark else PrimaryGreenSurface
            QuizCardState.CORRECT -> if (isDark) SuccessGreenSurfaceDark else SuccessGreenLight
            QuizCardState.WRONG -> if (isDark) ErrorRedSurfaceDark else ErrorRedLight
            QuizCardState.DISABLED -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(300)
    )

    val borderColor by animateColorAsState(
        targetValue = when (state) {
            QuizCardState.DEFAULT -> MaterialTheme.colorScheme.outline
            QuizCardState.SELECTED -> PrimaryGreen
            QuizCardState.CORRECT -> SuccessGreen
            QuizCardState.WRONG -> ErrorRed
            QuizCardState.DISABLED -> MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(300)
    )

    val labelBgColor by animateColorAsState(
        targetValue = when (state) {
            // Use theme primary so it's PrimaryGreenLight (#2D8F5E) in dark, PrimaryGreen in light
            QuizCardState.SELECTED -> MaterialTheme.colorScheme.primary
            QuizCardState.CORRECT -> SuccessGreen
            QuizCardState.WRONG -> ErrorRed
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(300)
    )

    // Colored circles always use white text: contrast ratios are 5.8–15:1 across all states/themes
    val labelTextColor = when (state) {
        QuizCardState.SELECTED, QuizCardState.CORRECT, QuizCardState.WRONG -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = state != QuizCardState.DISABLED) { onClick() },
        shape = QuizCardShape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.5.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Letter label circle
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = labelBgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = labelTextColor
                    )
                }
            }

            // Answer text
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun QuizCardPreview() {
    PreviewSurface {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            QuizCard(label = "A", text = "Default answer option")
            QuizCard(label = "B", text = "Selected answer option", state = QuizCardState.SELECTED)
            QuizCard(label = "C", text = "Correct answer option", state = QuizCardState.CORRECT)
            QuizCard(label = "D", text = "Wrong answer option", state = QuizCardState.WRONG)
        }
    }
}
