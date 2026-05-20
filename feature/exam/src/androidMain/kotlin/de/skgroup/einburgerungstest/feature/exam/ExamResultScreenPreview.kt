package de.skgroup.einburgerungstest.feature.exam

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.core.model.Answer
import de.skgroup.einburgerungstest.core.model.ExamResult
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Question
import de.skgroup.einburgerungstest.core.model.Topic
import de.skgroup.einburgerungstest.core.model.WrongAnswer
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun ExamResultScreenPreview() {
    PreviewSurface {
        ExamResultScreen(result = previewExamResult())
    }
}

@Preview
@Composable
private fun ExamResultScreenDarkPreview() {
    PreviewSurface(darkTheme = true) {
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

@Preview
@Composable
private fun WrongAnswersSummaryCardDarkPreview() {
    PreviewSurface(darkTheme = true) {
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
