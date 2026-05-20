package de.skgroup.einburgerungstest.feature.learn

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.core.model.TopicProgress
import de.skgroup.einburgerungstest.core.model.Topic
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun LearnScreenContentPreview() {
    PreviewSurface {
        LearnScreenContent(
            uiState = LearnUiState(
                topicProgressList = previewTopicProgressList(),
                bookmarkCount = 7,
                isLoading = false
            )
        )
    }
}

@Preview
@Composable
private fun LearnScreenContentDarkPreview() {
    PreviewSurface(darkTheme = true) {
        LearnScreenContent(
            uiState = LearnUiState(
                topicProgressList = previewTopicProgressList(),
                bookmarkCount = 7,
                isLoading = false
            )
        )
    }
}

private fun previewTopicProgressList() = listOf(
    TopicProgress(
        topic = Topic.DEMOCRACY_AND_STATE,
        totalQuestions = 43,
        answeredCorrectly = 18,
        answeredWrong = 5,
        totalAnswered = 23
    ),
    TopicProgress(
        topic = Topic.RIGHTS_AND_DUTIES,
        totalQuestions = 38,
        answeredCorrectly = 22,
        answeredWrong = 4,
        totalAnswered = 26
    ),
    TopicProgress(
        topic = Topic.HISTORY,
        totalQuestions = 52,
        answeredCorrectly = 13,
        answeredWrong = 6,
        totalAnswered = 19
    )
)
