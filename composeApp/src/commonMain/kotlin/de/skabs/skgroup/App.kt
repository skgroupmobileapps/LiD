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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.koinInject

/**
 * Manual ViewModel injection pattern that works on iOS with Kotlin 2.3.0+ IR backend.
 * Combines Koin injection with Compose's ViewModel lifecycle management.
 */
@Composable
inline fun <reified T : ViewModel> koinInjectViewModel(): T {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    
    // Get ViewModel instance from Koin
    val viewModelInstance: T = koinInject()
    
    // Create a factory that returns the Koin-injected instance
    val factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <V : ViewModel> create(modelClass: kotlin.reflect.KClass<V>, extras: CreationExtras): V {
            return viewModelInstance as V
        }
    }
    
    // Use viewModel with the factory
    return viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        key = T::class.simpleName,
        factory = factory
    )
}

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
    val hasCompletedOnboarding = settings.hasCompletedOnboarding
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
                                        }
                                        launchSingleTop = true
                                        restoreState = true
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
                        onLanguageChanged = { language ->
                            val updatedSettings = settings.copy(language = language)
                            settingsRepository.saveSettings(updatedSettings)
                        },
                        onComplete = { language, state ->
                            val currentSettings = settings.copy(
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
                    val viewModel: HomeViewModel = koinInjectViewModel()
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
                    val viewModel: LearnViewModel = koinInjectViewModel()
                    LearnScreen(
                        viewModel = viewModel,
                        onTopicSelected = { topic ->
                            navController.navigate(LearnQuestionRoute(mode = "TOPIC", topicId = topic.name))
                        },
                        onBookmarksClick = { /* Navigate to bookmarked questions */ },
                        onAllQuestionsClick = {
                            navController.navigate(LearnQuestionRoute(mode = "ALL", topicId = null))
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                composable<LearnQuestionRoute> { backStackEntry ->
                    // Extract arguments from the route using toRoute
                    val route: LearnQuestionRoute = backStackEntry.toRoute()
                    val mode: String = route.mode
                    val topicId: String? = route.topicId
                    
                    val viewModel: LearnViewModel = koinInjectViewModel()
                    
                    LaunchedEffect(mode, topicId) {
                        if (mode == "TOPIC" && topicId != null) {
                            try {
                                val cleanTopicId = topicId.trim('"', '{', '}').trim()
                                val topic = de.skabs.skgroup.core.model.Topic.valueOf(cleanTopicId)
                                viewModel.loadQuestionsForTopic(topic)
                            } catch (e: Exception) {
                                println("Failed to parse topic: $topicId, error: $e")
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
                    val viewModel: ExamViewModel = koinInjectViewModel()
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
                    val viewModel: ProfileViewModel = koinInjectViewModel()
                    ProfileScreen(viewModel = viewModel)
                }
            }
        }
        }
    }
}