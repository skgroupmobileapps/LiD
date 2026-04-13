package de.skgroup.einburgerungstest.feature.exam

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.designsystem.components.AppButton
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface
import de.skgroup.einburgerungstest.designsystem.theme.*
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview


/**
 * Exam intro screen shown before starting the exam.
 * Displays rules and a "Start Exam" button at the bottom.
 */
@Composable
fun ExamIntroScreen(
    federalState: FederalState,
    onStartExam: () -> Unit = {},
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        // Icon
        Surface(
            modifier = Modifier.size(80.dp),
            shape = RoundedCornerShape(20.dp),
            color = PrimaryGreen.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("🎓", style = MaterialTheme.typography.displayMedium)
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(Res.string.exam_intro_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.exam_intro_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(28.dp))

        // Info cards
        ExamInfoItem(icon = "📝", title = stringResource(Res.string.exam_33_questions), subtitle = stringResource(Res.string.exam_33_questions_subtitle, federalState.displayName))
        Spacer(Modifier.height(10.dp))
        ExamInfoItem(icon = "⏱", title = stringResource(Res.string.exam_60_minutes), subtitle = stringResource(Res.string.exam_60_minutes_subtitle))
        Spacer(Modifier.height(10.dp))
        ExamInfoItem(icon = "✅", title = stringResource(Res.string.exam_17_to_pass), subtitle = stringResource(Res.string.exam_17_to_pass_subtitle))
        Spacer(Modifier.height(10.dp))
        ExamInfoItem(icon = "🔇", title = stringResource(Res.string.exam_no_feedback), subtitle = stringResource(Res.string.exam_no_feedback_subtitle))

        Spacer(Modifier.weight(1f))

        // Start Exam button at the bottom
        AppButton(
            text = stringResource(Res.string.exam_start),
            trailingIcon = "🚀",
            onClick = onStartExam
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ExamInfoItem(
    icon: String,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(icon, style = MaterialTheme.typography.titleLarge)
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
private fun ExamIntroScreenPreview() {
    PreviewSurface {
        ExamIntroScreen(federalState = FederalState.BERLIN)
    }
}

@Preview
@Composable
private fun ExamInfoItemPreview() {
    PreviewSurface {
        ExamInfoItem(
            icon = "📝",
            title = "33 questions",
            subtitle = "30 general questions plus 3 for Berlin"
        )
    }
}
