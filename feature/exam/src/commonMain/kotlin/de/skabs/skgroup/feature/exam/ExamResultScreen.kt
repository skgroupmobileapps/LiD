package de.skabs.skgroup.feature.exam

import androidx.compose.foundation.layout.*
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
import de.skabs.skgroup.core.model.Answer
import de.skabs.skgroup.core.model.ExamResult
import de.skabs.skgroup.core.model.FederalState
import de.skabs.skgroup.core.model.Question
import de.skabs.skgroup.core.model.Topic
import de.skabs.skgroup.core.model.WrongAnswer
import de.skabs.skgroup.core.util.Timer
import de.skabs.skgroup.designsystem.components.*
import de.skabs.skgroup.designsystem.theme.*
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview



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
        val resultEmoji = if (result.passed) "🎉" else "😔"
        val resultColor = if (result.passed) SuccessGreen else ErrorRed

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
private fun WrongAnswersSummaryCard(result: ExamResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ErrorRedLight),
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
                    color = ErrorRed
                )
            }
            Spacer(Modifier.height(12.dp))

            result.wrongAnswers.take(5).forEach { wrong ->
                Text(
                    text = "• Q${wrong.question.id}: ${wrong.question.text.take(60)}...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            if (result.wrongAnswers.size > 5) {
                Text(
                    text = "... and ${result.wrongAnswers.size - 5} more",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = ErrorRed,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ExamResultScreenPreview() {
    PreviewSurface {
        ExamResultScreen(result = previewExamResult())
    }
}

@Preview
@Composable
private fun WrongAnswersSummaryCardPreview() {
    PreviewSurface {
        WrongAnswersSummaryCard(result = previewExamResult())
    }
}

private fun previewExamResult(): ExamResult {
    val wrongQuestion = Question(
        id = 21,
        text = "Which institution passes federal laws in Germany?",
        answers = listOf(
            Answer("A", "The Bundestag"),
            Answer("B", "The Bundesbank"),
            Answer("C", "The European Council"),
            Answer("D", "The Federal President alone")
        ),
        correctAnswerIndex = 0,
        topic = Topic.DEMOCRACY_AND_STATE,
        explanation = "Federal laws are passed through the parliamentary process led by the Bundestag."
    )

    return ExamResult(
        sessionId = 7,
        totalQuestions = 33,
        correctCount = 24,
        wrongCount = 9,
        passed = true,
        scorePercent = 72.7f,
        timeSpentMs = 38 * 60 * 1000L,
        wrongAnswers = listOf(
            WrongAnswer(wrongQuestion, selectedAnswerIndex = 2, correctAnswerIndex = 0),
            WrongAnswer(wrongQuestion.copy(id = 29, text = "How many federal states does Germany have?"), selectedAnswerIndex = 1, correctAnswerIndex = 0)
        ),
        federalState = FederalState.BERLIN,
        timestampMs = 0L
    )
}


