package de.skgroup.einburgerungstest.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Language
import de.skgroup.einburgerungstest.core.model.ThemeMode
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface

@Preview
@Composable
private fun OnboardingScreenPreview() {
    PreviewSurface {
        OnboardingScreen()
    }
}

@Preview
@Composable
private fun OnboardingScreenDarkPreview() {
    PreviewSurface(darkTheme = true) {
        OnboardingScreen()
    }
}

@Preview
@Composable
private fun WelcomeStepPreview() {
    PreviewSurface {
        WelcomeStep(onNext = {})
    }
}

@Preview
@Composable
private fun WelcomeStepDarkPreview() {
    PreviewSurface(darkTheme = true) {
        WelcomeStep(onNext = {})
    }
}

@Preview
@Composable
private fun LanguageStepPreview() {
    PreviewSurface {
        LanguageStep(
            selectedLanguage = Language.ENGLISH,
            onLanguageSelected = {},
            onNext = {}
        )
    }
}

@Preview
@Composable
private fun LanguageStepDarkPreview() {
    PreviewSurface(darkTheme = true) {
        LanguageStep(
            selectedLanguage = Language.ENGLISH,
            onLanguageSelected = {},
            onNext = {}
        )
    }
}

@Preview
@Composable
private fun StateStepPreview() {
    PreviewSurface {
        StateStep(
            selectedState = FederalState.HAMBURG,
            onStateSelected = {},
            onComplete = {}
        )
    }
}

@Preview
@Composable
private fun StateStepDarkPreview() {
    PreviewSurface(darkTheme = true) {
        StateStep(
            selectedState = FederalState.HAMBURG,
            onStateSelected = {},
            onComplete = {}
        )
    }
}

@Preview
@Composable
private fun ThemeStepPreview() {
    PreviewSurface {
        ThemeStep(
            selectedTheme = ThemeMode.SYSTEM,
            onThemeSelected = {},
            onComplete = {}
        )
    }
}

@Preview
@Composable
private fun ThemeStepDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ThemeStep(
            selectedTheme = ThemeMode.DARK,
            onThemeSelected = {},
            onComplete = {}
        )
    }
}
