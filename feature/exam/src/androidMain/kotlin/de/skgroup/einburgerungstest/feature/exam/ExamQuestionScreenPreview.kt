package de.skgroup.einburgerungstest.feature.exam

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.core.model.Answer
import de.skgroup.einburgerungstest.core.model.ExamSession
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Question
import de.skgroup.einburgerungstest.core.model.Topic
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun ExamQuestionScreenContentPreview() {
    PreviewSurface {
        ExamQuestionScreenContent(uiState = previewExamUiState())
    }
}

@Preview
@Composable
private fun ExamQuestionScreenContentDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ExamQuestionScreenContent(uiState = previewExamUiState())
    }
}

@Preview
@Composable
private fun ExamQuestionTopBarPreview() {
    PreviewSurface {
        ExamQuestionTopBar(
            currentIndex = 4,
            totalQuestions = 33,
            remainingTimeMs = 47 * 60 * 1000L,
            onClose = {}
        )
    }
}

@Preview
@Composable
private fun ExamQuestionTopBarDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ExamQuestionTopBar(
            currentIndex = 4,
            totalQuestions = 33,
            remainingTimeMs = 47 * 60 * 1000L,
            onClose = {}
        )
    }
}

@Preview
@Composable
private fun ExamQuestionNavigationRowPreview() {
    PreviewSurface {
        ExamQuestionNavigationRow(
            currentIndex = 32,
            totalQuestions = 33,
            onPreviousQuestion = {},
            onNextQuestion = {},
            onFinishExam = {}
        )
    }
}

@Preview
@Composable
private fun ExamQuestionNavigationRowDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ExamQuestionNavigationRow(
            currentIndex = 32,
            totalQuestions = 33,
            onPreviousQuestion = {},
            onNextQuestion = {},
            onFinishExam = {}
        )
    }
}

private fun previewExamUiState(): ExamUiState {
    val questions = listOf(previewExamQuestion(), previewExamQuestion(id = 2))
    return ExamUiState(
        phase = ExamPhase.IN_PROGRESS,
        session = ExamSession(
            questions = questions,
            answers = mutableMapOf(1 to 1),
            startTimeMs = 0L,
            federalState = FederalState.BERLIN
        ),
        currentQuestionIndex = 0,
        selectedAnswerIndex = 1,
        remainingTimeMs = 47 * 60 * 1000L
    )
}

private fun previewExamQuestion(id: Int = 1) = Question(
    id = id,
    text = "What is the role of the Bundestag in Germany?",
    answers = listOf(
        Answer("A", "It elects local mayors only"),
        Answer("B", "It is the federal parliament"),
        Answer("C", "It controls only foreign policy"),
        Answer("D", "It appoints judges for each city")
    ),
    correctAnswerIndex = 1,
    topic = Topic.DEMOCRACY_AND_STATE,
    explanation = "The Bundestag is Germany's federal parliament."
)
