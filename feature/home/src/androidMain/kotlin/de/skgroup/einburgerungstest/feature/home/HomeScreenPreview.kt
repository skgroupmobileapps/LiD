package de.skgroup.einburgerungstest.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.core.model.UserProgress
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun HomeScreenContentPreview() {
    PreviewSurface {
        HomeScreenContent(uiState = HomeUiState(progress = previewHomeProgress(), isLoading = false))
    }
}

@Preview
@Composable
private fun HomeScreenContentDarkPreview() {
    PreviewSurface(darkTheme = true) {
        HomeScreenContent(uiState = HomeUiState(progress = previewHomeProgress(), isLoading = false))
    }
}

@Preview
@Composable
private fun HomeOverallProgressCardPreview() {
    PreviewSurface {
        HomeOverallProgressCard(progress = previewHomeProgress())
    }
}

@Preview
@Composable
private fun HomeOverallProgressCardDarkPreview() {
    PreviewSurface(darkTheme = true) {
        HomeOverallProgressCard(progress = previewHomeProgress())
    }
}

@Preview
@Composable
private fun HomeQuickActionsSectionPreview() {
    PreviewSurface {
        Column {
            HomeQuickActionsSection(
                progress = previewHomeProgress(),
                onContinueLearning = {},
                onExamMode = {},
                onByTopic = {},
                onBookmarks = {},
                onAllQuestions = {}
            )
        }
    }
}

@Preview
@Composable
private fun HomeQuickActionsSectionDarkPreview() {
    PreviewSurface(darkTheme = true) {
        Column {
            HomeQuickActionsSection(
                progress = previewHomeProgress(),
                onContinueLearning = {},
                onExamMode = {},
                onByTopic = {},
                onBookmarks = {},
                onAllQuestions = {}
            )
        }
    }
}

private fun previewHomeProgress() = UserProgress(
    totalAnswered = 128,
    totalCorrect = 96,
    accuracy = 75f,
    bookmarkCount = 14,
    dayStreak = 9,
    overallProgressPercent = 41.3f,
    totalQuestionsAvailable = 310
)
