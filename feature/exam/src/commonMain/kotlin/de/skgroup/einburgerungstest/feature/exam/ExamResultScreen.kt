package de.skgroup.einburgerungstest.feature.exam

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.core.model.Answer
import de.skgroup.einburgerungstest.core.model.ExamResult
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Question
import de.skgroup.einburgerungstest.core.model.Topic
import de.skgroup.einburgerungstest.core.model.WrongAnswer
import de.skgroup.einburgerungstest.core.util.Timer
import de.skgroup.einburgerungstest.designsystem.components.*
import de.skgroup.einburgerungstest.designsystem.theme.*
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource



/**
 * Exam result screen — shown after finishing the exam.
 * Shows pass/fail, score, time, and wrong answer review.
 */
@Composable
fun ExamResultScreen(
    result: ExamResult,
    onReviewWrongAnswers: () -> Unit = {},
    onBackToHome: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        // Pass/Fail icon
        val isDark = isSystemInDarkTheme()
        val resultEmoji = if (result.passed) "🎉" else "😔"
        // headlineLarge is large text (28sp bold) → 3:1 ratio required; both colors meet it
        // In dark mode use lighter variants for better readability on dark surfaces
        val resultColor = if (result.passed) {
            if (isDark) SuccessGreenTextDark else SuccessGreen
        } else {
            if (isDark) androidx.compose.ui.graphics.Color(0xFFFF8A80) else ErrorRed
        }

        Surface(
            modifier = Modifier.size(80.dp),
            shape = RoundedCornerShape(20.dp),
            color = resultColor.copy(alpha = 0.12f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(resultEmoji, style = MaterialTheme.typography.displayMedium)
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = if (result.passed) stringResource(Res.string.exam_result_passed) else stringResource(Res.string.exam_result_failed),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = resultColor
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = if (result.passed)
                stringResource(Res.string.exam_result_passed_message)
            else
                stringResource(Res.string.exam_result_failed_message, ExamResult.PASS_THRESHOLD - result.correctCount),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        // Stats grid
        ExamResultStatsRow(result = result)

        Spacer(Modifier.height(20.dp))

        // Wrong answers summary
        if (result.wrongAnswers.isNotEmpty()) {
            WrongAnswersSummaryCard(result = result)

            Spacer(Modifier.height(16.dp))

            AppOutlinedButton(
                text = stringResource(Res.string.exam_review_wrong),
                onClick = onReviewWrongAnswers
            )
        }

        Spacer(Modifier.height(12.dp))

        AppButton(
            text = stringResource(Res.string.exam_back_home),
            trailingIcon = "🏠",
            onClick = onBackToHome
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ExamResultStatsRow(result: ExamResult) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            icon = "✅",
            value = "${result.correctCount}/${result.totalQuestions}",
            label = stringResource(Res.string.profile_correct),
            iconBackground = SuccessGreen,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = "📊",
            value = "${result.scorePercent.toInt()}%",
            label = stringResource(Res.string.home_progress),
            iconBackground = AccentGold,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = "⏱",
            value = Timer.formatTime(result.timeSpentMs),
            label = stringResource(Res.string.exam_result_time),
            iconBackground = AccentPurple,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
internal fun WrongAnswersSummaryCard(result: ExamResult) {
    val isDark = isSystemInDarkTheme()
    val cardBg = if (isDark) ErrorRedSurfaceDark else ErrorRedLight
    val titleColor = if (isDark) androidx.compose.ui.graphics.Color(0xFFFF8A80) else ErrorRed
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("❌", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${result.wrongCount} Wrong Answers",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor
                )
            }
            Spacer(Modifier.height(12.dp))

            result.wrongAnswers.take(5).forEach { wrong ->
                Text(
                    text = "• Q${wrong.question.id}: ${wrong.question.text.take(60)}...",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            if (result.wrongAnswers.size > 5) {
                Text(
                    text = "... and ${result.wrongAnswers.size - 5} more",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

