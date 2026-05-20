package de.skgroup.einburgerungstest.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun AnswerFeedbackCorrectPreview() {
    PreviewSurface {
        AnswerFeedback(
            isCorrect = true,
            explanation = "This answer matches the constitutional principle being tested."
        )
    }
}

@Preview
@Composable
private fun AnswerFeedbackCorrectDarkPreview() {
    PreviewSurface(darkTheme = true) {
        AnswerFeedback(
            isCorrect = true,
            explanation = "This answer matches the constitutional principle being tested."
        )
    }
}

@Preview
@Composable
private fun AnswerFeedbackWrongPreview() {
    PreviewSurface {
        AnswerFeedback(
            isCorrect = false,
            explanation = "The Basic Law protects freedom of expression, but it does not remove all legal limits."
        )
    }
}

@Preview
@Composable
private fun AnswerFeedbackWrongDarkPreview() {
    PreviewSurface(darkTheme = true) {
        AnswerFeedback(
            isCorrect = false,
            explanation = "The Basic Law protects freedom of expression, but it does not remove all legal limits."
        )
    }
}
