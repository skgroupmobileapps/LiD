package de.skabs.skgroup.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.skabs.skgroup.designsystem.theme.PrimaryGreen
import de.skabs.skgroup.designsystem.theme.ProgressBarShape
import de.skabs.skgroup.designsystem.theme.PreviewSurface
import androidx.compose.ui.tooling.preview.Preview

/**
 * Custom animated linear progress bar.
 *
 * @param progress Value between 0f and 1f
 * @param color Fill color
 * @param trackColor Background track color
 * @param height Height of the progress bar
 */
@Composable
fun AppProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = PrimaryGreen,
    trackColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
    height: Dp = 6.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(600)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(ProgressBarShape)
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .clip(ProgressBarShape)
                .background(color)
        )
    }
}

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
