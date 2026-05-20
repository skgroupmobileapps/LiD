package de.skgroup.einburgerungstest.feature.learn

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.core.model.Answer
import de.skgroup.einburgerungstest.core.model.Question
import de.skgroup.einburgerungstest.core.model.Topic
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface
import de.skgroup.einburgerungstest.domain.usecase.AnswerFeedback

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
private fun QuestionScreenContentDarkPreview() {
    PreviewSurface(darkTheme = true) {
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

@Preview
@Composable
private fun QuestionScreenAnsweredDarkPreview() {
    PreviewSurface(darkTheme = true) {
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
