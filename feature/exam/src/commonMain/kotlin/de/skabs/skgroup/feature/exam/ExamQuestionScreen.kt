package de.skabs.skgroup.feature.exam

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.skabs.skgroup.core.util.BackHandler
import de.skabs.skgroup.core.util.Timer
import de.skabs.skgroup.designsystem.components.*
import de.skabs.skgroup.designsystem.theme.PrimaryGreen
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/**
 * Exam question screen — NO answer checking.
 *
 * Key behavioral difference from learn mode:
 * - Green highlight when selected (not correct/wrong feedback)
 * - No explanation shown
 * - Timer visible, questions can be navigated freely
 * - Close icon in top-right with confirmation dialog
 * - Back gesture intercepted with confirmation dialog
 */
@Composable
fun ExamQuestionScreen(
    viewModel: ExamViewModel,
    onExitExam: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    ExamQuestionScreenContent(
        uiState = uiState,
        onSelectAnswer = { qId, index -> viewModel.selectAnswer(qId, index) },
        onNextQuestion = { viewModel.nextQuestion() },
        onPreviousQuestion = { viewModel.previousQuestion() },
        onFinishExam = { viewModel.finishExam() },
        onExitExam = onExitExam,
        modifier = modifier
    )
}

@Composable
fun ExamQuestionScreenContent(
    uiState: ExamUiState,
    onSelectAnswer: (Int, Int) -> Unit = { _, _ -> },
    onNextQuestion: () -> Unit = {},
    onPreviousQuestion: () -> Unit = {},
    onFinishExam: () -> Unit = {},
    onExitExam: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val session = uiState.session ?: return
    val questions = session.questions
    val currentIndex = uiState.currentQuestionIndex
    val currentQuestion = questions.getOrNull(currentIndex) ?: return

    // Exit confirmation dialog state
    var showExitDialog by remember { mutableStateOf(false) }

    // Intercept back gesture to show exit confirmation
    BackHandler(enabled = true) {
        showExitDialog = true
    }

    // Exit confirmation dialog
    if (showExitDialog) {
        ConfirmationDialog(
            title = stringResource(Res.string.dialog_leave_exam_title),
            message = stringResource(Res.string.dialog_leave_exam_message),
            confirmText = stringResource(Res.string.dialog_leave),
            dismissText = stringResource(Res.string.dialog_stay),
            onConfirm = {
                showExitDialog = false
                onExitExam()
            },
            onDismiss = { showExitDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        // Timer, progress, and close button row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.exam_question_of, currentIndex + 1, questions.size),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Timer
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "⏱ ${Timer.formatTime(uiState.remainingTimeMs)}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                // Close button
                IconButton(onClick = { showExitDialog = true }) {
                    Text(
                        text = "✕",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Progress bar
        AppProgressBar(
            progress = (currentIndex + 1).toFloat() / questions.size,
            color = PrimaryGreen
        )

        Spacer(Modifier.height(20.dp))

        // Question text
        Text(
            text = currentQuestion.text,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(24.dp))

        // Answer options (NO correct/wrong, just SELECTED or DEFAULT)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            currentQuestion.answers.forEachIndexed { index, answer ->
                val isSelected = uiState.selectedAnswerIndex == index
                QuizCard(
                    label = answer.label,
                    text = answer.text,
                    state = if (isSelected) QuizCardState.SELECTED else QuizCardState.DEFAULT,
                    onClick = { onSelectAnswer(currentQuestion.id, index) }
                )
            }
        }

        // Navigation buttons
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (currentIndex > 0) {
                AppOutlinedButton(
                    text = stringResource(Res.string.exam_previous),
                    onClick = onPreviousQuestion,
                    modifier = Modifier.weight(1f)
                )
            }
            if (currentIndex < questions.size - 1) {
                AppButton(
                    text = stringResource(Res.string.exam_next),
                    onClick = onNextQuestion,
                    modifier = Modifier.weight(1f)
                )
            } else {
                AppButton(
                    text = stringResource(Res.string.exam_finish),
                    trailingIcon = "✓",
                    onClick = onFinishExam,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}
