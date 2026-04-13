package de.skgroup.einburgerungstest.navigation

import kotlinx.serialization.Serializable

/**
 * Navigation routes for the app.
 */
@Serializable
sealed class Screen {
    @Serializable
    data object Onboarding : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object Learn : Screen()

    @Serializable
    data object LearnQuestion : Screen()

    @Serializable
    data object ExamIntro : Screen()

    @Serializable
    data object ExamQuestion : Screen()

    @Serializable
    data object ExamResult : Screen()

    @Serializable
    data object Profile : Screen()
}

/**
 * Bottom navigation tabs.
 */
enum class BottomNavTab(val label: String, val icon: String, val route: Screen) {
    HOME("Home", "🏠", Screen.Home),
    LEARN("Learn", "📚", Screen.Learn),
    EXAM("Exam", "🎓", Screen.ExamIntro),
    PROFILE("Profile", "👤", Screen.Profile)
}
