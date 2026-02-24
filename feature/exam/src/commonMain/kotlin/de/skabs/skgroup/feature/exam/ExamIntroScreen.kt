package de.skabs.skgroup.feature.exam

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
import de.skabs.skgroup.core.model.FederalState
import de.skabs.skgroup.designsystem.components.AppButton
import de.skabs.skgroup.designsystem.theme.*



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
            text = "Exam Mode",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Simulate the official Einbürgerungstest",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(28.dp))

        // Info cards
        ExamInfoItem(icon = "📝", title = "33 Questions", subtitle = "30 general + 3 from ${federalState.displayName}")
        Spacer(Modifier.height(10.dp))
        ExamInfoItem(icon = "⏱", title = "60 Minutes", subtitle = "Timer counts down during the exam")
        Spacer(Modifier.height(10.dp))
        ExamInfoItem(icon = "✅", title = "17 to Pass", subtitle = "You need at least 17 correct answers (51%)")
        Spacer(Modifier.height(10.dp))
        ExamInfoItem(icon = "🔇", title = "No Feedback", subtitle = "Answers are not checked until the end")

        Spacer(Modifier.weight(1f))

        // Start Exam button at the bottom
        AppButton(
            text = "Start Exam",
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
