package de.skgroup.einburgerungstest.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.core.model.ExamHistoryEntry
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Language
import de.skgroup.einburgerungstest.core.model.ThemeMode
import de.skgroup.einburgerungstest.core.model.UserProgress
import de.skgroup.einburgerungstest.core.model.UserSettings
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface
import de.skgroup.einburgerungstest.domain.usecase.ExamStats

@Preview
@Composable
private fun ProfileScreenContentPreview() {
    PreviewSurface {
        ProfileScreenContent(uiState = previewProfileUiState())
    }
}

@Preview
@Composable
private fun ProfileScreenContentDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ProfileScreenContent(uiState = previewProfileUiState())
    }
}

@Preview
@Composable
private fun ProfileSettingsCardPreview() {
    PreviewSurface {
        ProfileSettingsCard(
            settings = previewProfileUiState().settings,
            onOpenLanguageDialog = {},
            onUpdateSettings = {},
            onResetStatistics = {}
        )
    }
}

@Preview
@Composable
private fun ProfileSettingsCardDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ProfileSettingsCard(
            settings = previewProfileUiState().settings,
            onOpenLanguageDialog = {},
            onUpdateSettings = {},
            onResetStatistics = {}
        )
    }
}

@Preview
@Composable
private fun ProfileAboutCardPreview() {
    PreviewSurface {
        ProfileAboutCard()
    }
}

@Preview
@Composable
private fun ProfileAboutCardDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ProfileAboutCard()
    }
}

private fun previewProfileUiState() = ProfileUiState(
    settings = UserSettings(
        language = Language.ENGLISH,
        federalState = FederalState.HAMBURG,
        themeMode = ThemeMode.SYSTEM,
        analyticsEnabled = true,
        hasCompletedOnboarding = true,
        isGuest = true
    ),
    progress = UserProgress(
        totalAnswered = 142,
        totalCorrect = 103,
        accuracy = 72.5f,
        bookmarkCount = 12,
        dayStreak = 11,
        overallProgressPercent = 46.1f,
        totalQuestionsAvailable = 310
    ),
    examStats = ExamStats(
        totalAttempts = 8,
        totalPassed = 6,
        averageScore = 74.8f
    ),
    examHistory = listOf(
        ExamHistoryEntry(
            id = 1,
            totalQuestions = 33,
            correctCount = 25,
            passed = true,
            scorePercent = 75.8f,
            timestampMs = 0L,
            federalState = FederalState.HAMBURG
        ),
        ExamHistoryEntry(
            id = 2,
            totalQuestions = 33,
            correctCount = 16,
            passed = false,
            scorePercent = 48.5f,
            timestampMs = 0L,
            federalState = FederalState.HAMBURG
        )
    ),
    isLoading = false
)
