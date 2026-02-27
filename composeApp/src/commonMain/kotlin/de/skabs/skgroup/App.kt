package de.skabs.skgroup

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import de.skabs.skgroup.core.util.AppLocaleProvider
import de.skabs.skgroup.data.local.QuestionSeeder
import de.skabs.skgroup.data.repository.SettingsRepository
import de.skabs.skgroup.designsystem.theme.EinbuergerungTheme
import de.skabs.skgroup.feature.exam.ExamIntroScreen
import de.skabs.skgroup.feature.exam.ExamPhase
import de.skabs.skgroup.feature.exam.ExamQuestionScreen
import de.skabs.skgroup.feature.exam.ExamResultScreen
import de.skabs.skgroup.feature.exam.ExamViewModel
import de.skabs.skgroup.feature.home.HomeScreen
import de.skabs.skgroup.feature.home.HomeViewModel
import de.skabs.skgroup.feature.learn.LearnScreen
import de.skabs.skgroup.feature.learn.LearnViewModel
import de.skabs.skgroup.feature.learn.QuestionScreen
import de.skabs.skgroup.feature.onboarding.OnboardingScreen
import de.skabs.skgroup.feature.profile.ProfileScreen
import de.skabs.skgroup.feature.profile.ProfileViewModel
import de.skabs.skgroup.navigation.BottomNavTab
import kmpexam.resources.generated.resources.*
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject

/**
 * Route for learn question screen.
 */
@Serializable
data class LearnQuestionRoute(
    val mode: String = "ALL",
    val topicId: String? = null
)

@Composable
fun App(initialDeeplinkRoute: String? = null) {
    val settingsRepository: SettingsRepository = koinInject()
    val questionSeeder: QuestionSeeder = koinInject()
    val progressUseCase: de.skabs.skgroup.domain.usecase.ProgressUseCase = koinInject()
    val widgetSyncManager: de.skabs.skgroup.widget.WidgetSyncManager = koinInject()

    // Seed questions on first launch and sync widget stats
    LaunchedEffect(Unit) {
        questionSeeder.seedIfNeeded()
        
        // Sync widget stats after seeding completes
        try {
            val progress = progressUseCase.getUserProgress()
            val widgetStats = de.skabs.skgroup.core.model.WidgetStats(
                correctAnswers = progress.totalCorrect,
                accuracyPercent = progress.accuracy,
                dayStreak = progress.dayStreak
            )
            widgetSyncManager.syncStats(widgetStats)
        } catch (_: Exception) {
            // Silently ignore if stats aren't available yet
        }
    }

    // Observe settings reactively so dark mode toggle takes effect immediately
    val settings by settingsRepository.settingsFlow.collectAsState()
    val hasCompletedOnboarding = remember { settingsRepository.hasCompletedOnboarding() }
    val startDestination = if (hasCompletedOnboarding) "home" else "onboarding"

    AppLocaleProvider(language = settings.language) {
        EinbuergerungTheme(darkTheme = settings.darkMode) {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Handle deeplink navigation
            LaunchedEffect(initialDeeplinkRoute) {
                initialDeeplinkRoute?.let { route ->
                    // Only navigate if onboarding is complete
                    if (hasCompletedOnboarding && route in listOf("home", "learn", "exam_intro")) {
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                        }
                    }
                }
            }

            // Determine if bottom nav should be shown
            val showBottomNav = currentRoute in listOf("home", "learn", "exam_intro", "profile")

        Scaffold(
            bottomBar = {
                if (showBottomNav) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        BottomNavTab.entries.forEach { tab ->
                            val route = when (tab) {
                                BottomNavTab.HOME -> "home"
                                BottomNavTab.LEARN -> "learn"
                                BottomNavTab.EXAM -> "exam_intro"
                                BottomNavTab.PROFILE -> "profile"
                            }
                            val localizedLabel = when (tab) {
                                BottomNavTab.HOME -> stringResource(Res.string.nav_home)
                                BottomNavTab.LEARN -> stringResource(Res.string.nav_learn)
                                BottomNavTab.EXAM -> stringResource(Res.string.nav_exam)
                                BottomNavTab.PROFILE -> stringResource(Res.string.nav_profile)
                            }
                            NavigationBarItem(
                                selected = currentRoute == route,
                                onClick = {
                                    navController.navigate(route) {
                                        popUpTo("home") {
                                            saveState = true
                                            inclusive = true
                                        }
                                        launchSingleTop = false
                                        restoreState = false
                                    }
                                },
                                icon = { Text(tab.icon) },
                                label = { Text(localizedLabel, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("onboarding") {
                    OnboardingScreen(
                        onComplete = { language, state ->
                            val currentSettings = settingsRepository.loadSettings().copy(
                                language = language,
                                federalState = state,
                                hasCompletedOnboarding = true
                            )
                            settingsRepository.saveSettings(currentSettings)
                            navController.navigate("home") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    )
                }

                composable("home") {
                    val viewModel: HomeViewModel = koinViewModel()
                    HomeScreen(
                        viewModel = viewModel,
                        onContinueLearning = { navController.navigate(LearnQuestionRoute(mode = "ALL", topicId = null)) },
                        onExamMode = { navController.navigate("exam_intro") },
                        onByTopic = { navController.navigate("learn") },
                        onBookmarks = { navController.navigate("learn") },
                        onAllQuestions = { navController.navigate(LearnQuestionRoute(mode = "ALL", topicId = null)) }
                    )
                }

                composable("learn") {
                    val viewModel: LearnViewModel = koinViewModel()
                    LearnScreen(
                        viewModel = viewModel,
                        onTopicSelected = { topic ->
                            navController.navigate(LearnQuestionRoute(mode = "TOPIC", topicId = topic.name))
                        },
                        onBookmarksClick = { /* Navigate to bookmarked questions */ },
                        onAllQuestionsClick = {
                            navController.navigate(LearnQuestionRoute(mode = "ALL", topicId = null))
                        }
                    )
                }

                composable<LearnQuestionRoute> { backStackEntry ->
                    // Extract arguments from the route using toRoute
                    val route: LearnQuestionRoute = backStackEntry.toRoute()
                    val mode: String = route.mode
                    val topicId: String? = route.topicId
                    
                    val viewModel: LearnViewModel = koinViewModel()
                    
                    LaunchedEffect(mode, topicId) {
                        if (mode == "TOPIC" && topicId != null) {
                            try {
                                val topic = de.skabs.skgroup.core.model.Topic.valueOf(topicId)
                                viewModel.loadQuestionsForTopic(topic)
                            } catch (_: Exception) {
                                viewModel.loadAllQuestions()
                            }
                        } else {
                            viewModel.loadAllQuestions()
                        }
                    }

                    QuestionScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onClose = { navController.popBackStack() }
                    )
                }

                composable("exam_intro") {
                    val viewModel: ExamViewModel = koinViewModel()
                    val uiState by viewModel.uiState.collectAsState()

                    when (uiState.phase) {
                        ExamPhase.INTRO -> ExamIntroScreen(
                            federalState = uiState.federalState,
                            onStartExam = { viewModel.startExam() },
                            onBack = { navController.popBackStack() }
                        )
                        ExamPhase.IN_PROGRESS -> ExamQuestionScreen(
                            viewModel = viewModel,
                            onExitExam = {
                                viewModel.resetExam()
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                        ExamPhase.RESULT -> {
                            uiState.result?.let { result ->
                                ExamResultScreen(
                                    result = result,
                                    onReviewWrongAnswers = { /* Navigate to review */ },
                                    onBackToHome = {
                                        viewModel.resetExam()
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                composable("profile") {
                    val viewModel: ProfileViewModel = koinViewModel()
                    ProfileScreen(viewModel = viewModel)
                }
            }
        }
        }
    }
}