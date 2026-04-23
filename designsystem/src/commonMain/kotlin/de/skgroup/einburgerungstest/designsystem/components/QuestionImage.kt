package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Maps question imageName strings to Compose drawable resources.
 */
private val questionImageResources: Map<String, DrawableResource> = mapOf(
    // General questions
    "q_21.png" to Res.drawable.q_21,
    "q_55.png" to Res.drawable.q_55,
    "q_70.png" to Res.drawable.q_70,
    "q_130.png" to Res.drawable.q_130,
    "q_176.png" to Res.drawable.q_176,
    "q_181.png" to Res.drawable.q_181,
    "q_187.png" to Res.drawable.q_187,
    "q_209.png" to Res.drawable.q_209,
    "q_216.png" to Res.drawable.q_216,
    "q_226.png" to Res.drawable.q_226,
    "q_235.png" to Res.drawable.q_235,
    // State questions (position 1 and 8 per Land)
    "q_301.png" to Res.drawable.q_301,
    "q_308.png" to Res.drawable.q_308,
    "q_311.png" to Res.drawable.q_311,
    "q_318.png" to Res.drawable.q_318,
    "q_321.png" to Res.drawable.q_321,
    "q_328.png" to Res.drawable.q_328,
    "q_331.png" to Res.drawable.q_331,
    "q_338.png" to Res.drawable.q_338,
    "q_341.png" to Res.drawable.q_341,
    "q_348.png" to Res.drawable.q_348,
    "q_351.png" to Res.drawable.q_351,
    "q_358.png" to Res.drawable.q_358,
    "q_361.png" to Res.drawable.q_361,
    "q_368.png" to Res.drawable.q_368,
    "q_371.png" to Res.drawable.q_371,
    "q_378.png" to Res.drawable.q_378,
    "q_381.png" to Res.drawable.q_381,
    "q_388.png" to Res.drawable.q_388,
    "q_391.png" to Res.drawable.q_391,
    "q_398.png" to Res.drawable.q_398,
    "q_401.png" to Res.drawable.q_401,
    "q_408.png" to Res.drawable.q_408,
    "q_411.png" to Res.drawable.q_411,
    "q_418.png" to Res.drawable.q_418,
    "q_421.png" to Res.drawable.q_421,
    "q_428.png" to Res.drawable.q_428,
    "q_431.png" to Res.drawable.q_431,
    "q_438.png" to Res.drawable.q_438,
    "q_441.png" to Res.drawable.q_441,
    "q_448.png" to Res.drawable.q_448,
    "q_451.png" to Res.drawable.q_451,
    "q_458.png" to Res.drawable.q_458,
)

/**
 * Displays a question's associated image in a rounded card.
 * Tapping the image opens a fullscreen zoomable viewer.
 * Renders nothing when [imageName] is null or not found.
 */
@Composable
fun QuestionImage(
    imageName: String?,
    modifier: Modifier = Modifier
) {
    if (imageName == null) return

    val drawableRes = questionImageResources[imageName] ?: return

    var showFullscreen by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showFullscreen = true },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
    ) {
        Image(
            painter = painterResource(drawableRes),
            contentDescription = "Question image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 220.dp)
                .padding(8.dp)
        )
    }

    if (showFullscreen) {
        FullscreenImageViewer(
            drawableRes = drawableRes,
            onDismiss = { showFullscreen = false }
        )
    }
}
