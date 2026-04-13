package de.skgroup.einburgerungstest.feature.learn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.core.model.Answer
import de.skgroup.einburgerungstest.designsystem.components.*
import de.skgroup.einburgerungstest.designsystem.theme.PrimaryGreen
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview

/**
 * Learn mode question screen — immediate answer checking with feedback.
 *
 * When user selects an answer:
 * - Correct → green highlight + "Correct!" badge
 * - Wrong → red highlight + "Wrong!" badge + explanation text
 */
import de.skgroup.einburgerungstest.core.model.Question
import de.skgroup.einburgerungstest.core.model.Topic
import de.skgroup.einburgerungstest.domain.usecase.AnswerFeedback
@Composable
fun QuestionScreen(
    viewModel: LearnViewModel,
    onBack: () -> Unit = {},
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    QuestionScreenContent(
        uiState = uiState,
        onToggleBookmark = { viewModel.toggleBookmark(it) },
        onSubmitAnswer = { q, i -> viewModel.submitAnswer(q, i) },
        onNextQuestion = { viewModel.nextQuestion() },
        onBack = onBack,
        onClose = onClose,
        modifier = modifier
    )
}

@Composable
fun QuestionScreenContent(
    uiState: LearnUiState,
    onToggleBookmark: (Int) -> Unit = {},
    onSubmitAnswer: (Question, Int) -> Unit = { _, _ -> },
    onNextQuestion: () -> Unit = {},
    onBack: () -> Unit = {},
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val questions = uiState.currentQuestions
    val currentIndex = uiState.currentQuestionIndex
    
    // Show loading indicator while questions are being loaded
    if (uiState.isLoading || questions.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
        return
    }
    
    val currentQuestion = questions.getOrNull(currentIndex) ?: return
    val feedback = uiState.feedback
    val hasAnswered = feedback != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // Progress indicator with bookmark and close
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bookmark toggle
                val isBookmarked = uiState.bookmarkedQuestions.any { it.id == currentQuestion.id }
                IconButton(onClick = { onToggleBookmark(currentQuestion.id) }) {
                    Text(
                        text = if (isBookmarked) "🔖" else "📑",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                // Close button
                IconButton(onClick = onClose) {
                    Text(
                        text = "✕",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

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

        // Answer options (scrollable area)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            currentQuestion.answers.forEachIndexed { index, answer ->
                val state = when {
                    !hasAnswered -> {
                        if (uiState.selectedAnswerIndex == index) QuizCardState.SELECTED
                        else QuizCardState.DEFAULT
                    }
                    index == currentQuestion.correctAnswerIndex -> QuizCardState.CORRECT
                    index == feedback?.selectedIndex -> QuizCardState.WRONG
                    else -> QuizCardState.DISABLED
                }

                QuizCard(
                    label = answer.label,
                    text = answer.text,
                    state = state,
                    onClick = {
                        if (!hasAnswered) {
                            onSubmitAnswer(currentQuestion, index)
                        }
                    }
                )
            }

            // Feedback section
            AnimatedVisibility(visible = hasAnswered) {
                feedback?.let { fb ->
                    Column {
                        Spacer(Modifier.height(12.dp))
                        AnswerFeedback(
                            isCorrect = fb.isCorrect,
                            explanation = fb.explanation
                        )
                    }
                }
            }
        }

        // Next button
        Spacer(Modifier.height(12.dp))
        if (hasAnswered) {
            val isLast = currentIndex >= questions.size - 1
            AppButton(
                text = if (isLast) stringResource(Res.string.exam_finish) else stringResource(Res.string.learn_next_question),
                trailingIcon = if (isLast) "✓" else "›",
                onClick = {
                    if (isLast) onBack() else onNextQuestion()
                }
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun QuestionScreenContentPreview() {
    PreviewSurface {
        QuestionScreenContent(
            uiState = previewQuestionUiState(answered = false)
        )
    }
}

@Preview
@Composable
private fun QuestionScreenAnsweredPreview() {
    PreviewSurface {
        QuestionScreenContent(
            uiState = previewQuestionUiState(answered = true)
        )
    }
}

private fun previewQuestionUiState(answered: Boolean): LearnUiState {
    val question = previewLearnQuestion()
    val feedback = if (answered) {
        AnswerFeedback(
            question = question,
            selectedIndex = 2,
            isCorrect = false,
            correctIndex = question.correctAnswerIndex,
            explanation = question.explanation
        )
    } else {
        null
    }

    return LearnUiState(
        currentQuestions = listOf(question, previewLearnQuestion(id = 2)),
        currentQuestionIndex = 0,
        selectedAnswerIndex = if (answered) 2 else 1,
        feedback = feedback,
        bookmarkedQuestions = listOf(question),
        bookmarkCount = 1,
        isLoading = false
    )
}

private fun previewLearnQuestion(id: Int = 1) = Question(
    id = id,
    text = "What is guaranteed by Article 5 of the German Basic Law?",
    answers = listOf(
        Answer("A", "Mandatory military service for everyone"),
        Answer("B", "Freedom of expression"),
        Answer("C", "The right to ignore elections"),
        Answer("D", "Unlimited state surveillance")
    ),
    correctAnswerIndex = 1,
    topic = Topic.RIGHTS_AND_DUTIES,
    explanation = "Article 5 of the Basic Law protects freedom of expression, press, and information."
)
