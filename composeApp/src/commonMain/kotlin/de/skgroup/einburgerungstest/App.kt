package de.skgroup.einburgerungstest

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import de.skgroup.einburgerungstest.core.util.AppLocaleProvider
import de.skgroup.einburgerungstest.data.local.QuestionSeeder
import de.skgroup.einburgerungstest.data.repository.SettingsRepository
import de.skgroup.einburgerungstest.designsystem.theme.EinbuergerungTheme
import de.skgroup.einburgerungstest.feature.exam.ExamIntroScreen
import de.skgroup.einburgerungstest.feature.exam.ExamPhase
import de.skgroup.einburgerungstest.feature.exam.ExamQuestionScreen
import de.skgroup.einburgerungstest.feature.exam.ExamResultScreen
import de.skgroup.einburgerungstest.feature.exam.ExamViewModel
import de.skgroup.einburgerungstest.feature.feedback.FeedbackDialog
import de.skgroup.einburgerungstest.feature.feedback.FeedbackPhase
import de.skgroup.einburgerungstest.feature.feedback.FeedbackViewModel
import de.skgroup.einburgerungstest.feature.feedback.StoreRedirectDialog
import de.skgroup.einburgerungstest.feature.home.HomeScreen
import de.skgroup.einburgerungstest.feature.home.HomeViewModel
import de.skgroup.einburgerungstest.feature.learn.LearnScreen
import de.skgroup.einburgerungstest.feature.learn.LearnViewModel
import de.skgroup.einburgerungstest.feature.learn.QuestionScreen
import de.skgroup.einburgerungstest.feature.onboarding.OnboardingScreen
import de.skgroup.einburgerungstest.feature.profile.ProfileScreen
import de.skgroup.einburgerungstest.feature.profile.ProfileViewModel
import de.skgroup.einburgerungstest.navigation.BottomNavTab
import de.skgroup.einburgerungstest.tracking.TrackingClient
import de.skgroup.einburgerungstest.tracking.TrackingEvent
import kmpexam.resources.generated.resources.*
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import de.skgroup.einburgerungstest.designsystem.theme.PreviewSurface
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
    val progressUseCase: de.skgroup.einburgerungstest.domain.usecase.ProgressUseCase = koinInject()
    val widgetSyncManager: de.skgroup.einburgerungstest.widget.WidgetSyncManager = koinInject()
    val trackingClient: TrackingClient = koinInject()

    // Seed questions on first launch and sync widget stats
    LaunchedEffect(Unit) {
        questionSeeder.seedIfNeeded()
        
        // Sync widget stats after seeding completes
        try {
            val progress = progressUseCase.getUserProgress()
            val widgetStats = de.skgroup.einburgerungstest.core.model.WidgetStats(
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

    // Bug 1 + 5 fix: navController lives outside AppLocaleProvider so key(localeCode) inside
    // AppLocaleProvider never destroys or resets the navigation back stack on locale changes.
    val navController = rememberNavController()

    AppLocaleProvider(language = settings.language) {
        EinbuergerungTheme(darkTheme = settings.darkMode) {
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
                    AppBottomNavigationBar(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo("home") {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("onboarding") {
                    LaunchedEffect(Unit) {
                        trackingClient.track(TrackingEvent.OnboardingStarted)
                        trackingClient.track(TrackingEvent.ScreenView("onboarding"))
                    }
                    OnboardingScreen(
                        onLanguageChanged = { language ->
                            // Only track the event — the language is persisted together with
                            // federalState in onComplete. Saving here would change settings.language,
                            // which triggers key(localeCode) in AppLocaleProvider and would recreate
                            // the NavHost, resetting currentStep back to 0 (Welcome screen).
                            trackingClient.track(TrackingEvent.LanguageSelected(language.code))
                        },
                        onFederalStateChanged = { state ->
                            trackingClient.track(TrackingEvent.FederalStateSelected(state.name))
                        },
                        onComplete = { language, state ->
                            val currentSettings = settings.copy(
                                language = language,
                                federalState = state,
                                hasCompletedOnboarding = true
                            )
                            settingsRepository.saveSettings(currentSettings)
                            trackingClient.track(
                                TrackingEvent.OnboardingCompleted(
                                    language = language.code,
                                    federalState = state.name
                                )
                            )
                            navController.navigate("home") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    )
                }

                composable("home") {
                    LaunchedEffect(Unit) {
                        trackingClient.track(TrackingEvent.ScreenView("home"))
                    }
                    val viewModel: HomeViewModel = koinInjectViewModel()
                    HomeScreen(
                        viewModel = viewModel,
                        onContinueLearning = {
                            navController.navigate(LearnQuestionRoute(mode = "ALL", topicId = null))
                        },
                        onExamMode = {
                            navController.navigate("exam_intro")
                        },
                        onByTopic = { navController.navigate("learn") },
                        onBookmarks = { navController.navigate("learn") },
                        onAllQuestions = {
                            navController.navigate(LearnQuestionRoute(mode = "ALL", topicId = null))
                        }
                    )
                }

                composable("learn") {
                    val viewModel: LearnViewModel = koinInjectViewModel()
                    LaunchedEffect(Unit) {
                        trackingClient.track(TrackingEvent.ScreenView("learn"))
                        // Refresh topic progress every time the Learn tab is opened so the
                        // list is never stale and isLoading is guaranteed to be reset.
                        viewModel.loadTopics()
                    }
                    LearnScreen(
                        viewModel = viewModel,
                        onTopicSelected = { topic ->
                            trackingClient.track(TrackingEvent.TopicSelected(topic.name))
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
                        val screenName = when (mode) {
                            "TOPIC" -> "learn_questions_topic"
                            "REVIEW" -> "learn_questions_review"
                            else -> "learn_questions_all"
                        }
                        trackingClient.track(TrackingEvent.ScreenView(screenName))
                    }
                    
                    LaunchedEffect(mode, topicId) {
                        when (mode) {
                            "TOPIC" -> if (topicId != null) {
                                try {
                                    val cleanTopicId = topicId.trim('"', '{', '}').trim()
                                    val topic = de.skgroup.einburgerungstest.core.model.Topic.valueOf(cleanTopicId)
                                    viewModel.loadQuestionsForTopic(topic)
                                } catch (e: Exception) {
                                    println("Failed to parse topic: $topicId, error: $e")
                                    viewModel.loadAllQuestions()
                                }
                            } else {
                                viewModel.loadAllQuestions()
                            }
                            "REVIEW" -> {
                                val ids = topicId
                                    ?.split(",")
                                    ?.mapNotNull { it.trim().toIntOrNull() }
                                    ?: emptyList()
                                viewModel.loadQuestionsForReview(ids)
                            }
                            else -> viewModel.loadAllQuestions()
                        }
                    }

                    QuestionScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onClose = { navController.popBackStack() }
                    )

                    // Learning feedback trigger
                    val learnState by viewModel.uiState.collectAsState()
                    if (learnState.showFeedbackTrigger) {
                        val feedbackViewModel: FeedbackViewModel = koinInjectViewModel()
                        val feedbackState by feedbackViewModel.uiState.collectAsState()

                        LaunchedEffect(Unit) {
                            feedbackViewModel.checkAndShowFeedbackAfterLearning()
                            viewModel.clearFeedbackTrigger()
                        }

                        when (feedbackState.phase) {
                            FeedbackPhase.FEEDBACK_DIALOG -> FeedbackDialog(
                                onSubmit = { rating, comment -> feedbackViewModel.submitFeedback(rating, comment) },
                                onDismiss = { feedbackViewModel.dismissFeedback() }
                            )
                            FeedbackPhase.STORE_REDIRECT -> StoreRedirectDialog(
                                onRateInStore = { feedbackViewModel.openStoreReview() },
                                onDismiss = { feedbackViewModel.declineStoreReview() }
                            )
                            else -> {}
                        }
                    }
                }

                composable("exam_intro") {
                    val viewModel: ExamViewModel = koinInjectViewModel()
                    val uiState by viewModel.uiState.collectAsState()

                    LaunchedEffect(uiState.phase) {
                        val screenName = when (uiState.phase) {
                            ExamPhase.INTRO -> "exam_intro"
                            ExamPhase.IN_PROGRESS -> "exam_questions"
                            ExamPhase.RESULT -> "exam_result"
                        }
                        trackingClient.track(TrackingEvent.ScreenView(screenName))
                    }

                    when (uiState.phase) {
                        ExamPhase.INTRO -> ExamIntroScreen(
                            federalState = uiState.federalState,
                            onStartExam = { viewModel.startExam() },
                            onBack = { navController.popBackStack() }
                        )
                        ExamPhase.IN_PROGRESS -> ExamQuestionScreen(
                            viewModel = viewModel,
                            onExitExam = {
                                viewModel.abandonExam()
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                        ExamPhase.RESULT -> {
                            uiState.result?.let { result ->
                                val feedbackViewModel: FeedbackViewModel = koinInjectViewModel()
                                val feedbackState by feedbackViewModel.uiState.collectAsState()

                                // Show feedback dialog 3 seconds after result appears
                                LaunchedEffect(result) {
                                    kotlinx.coroutines.delay(3000)
                                    feedbackViewModel.checkAndShowFeedbackAfterExam(result.passed)
                                }

                                ExamResultScreen(
                                    result = result,
                                    onReviewWrongAnswers = {
                                        val ids = result.wrongAnswers
                                            .map { it.question.id }
                                            .joinToString(",")
                                        navController.navigate(
                                            LearnQuestionRoute(mode = "REVIEW", topicId = ids)
                                        )
                                    },
                                    onBackToHome = {
                                        viewModel.resetExam()
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                )

                                when (feedbackState.phase) {
                                    FeedbackPhase.FEEDBACK_DIALOG -> FeedbackDialog(
                                        onSubmit = { rating, comment -> feedbackViewModel.submitFeedback(rating, comment) },
                                        onDismiss = { feedbackViewModel.dismissFeedback() }
                                    )
                                    FeedbackPhase.STORE_REDIRECT -> StoreRedirectDialog(
                                        onRateInStore = { feedbackViewModel.openStoreReview() },
                                        onDismiss = { feedbackViewModel.declineStoreReview() }
                                    )
                                    else -> {}
                                }
                            }
                        }
                    }
                }

                composable("profile") {
                    LaunchedEffect(Unit) {
                        trackingClient.track(TrackingEvent.ScreenView("profile"))
                    }
                    val viewModel: ProfileViewModel = koinInjectViewModel()
                    val feedbackViewModel: FeedbackViewModel = koinInjectViewModel()
                    val feedbackState by feedbackViewModel.uiState.collectAsState()

                    ProfileScreen(
                        viewModel = viewModel,
                        onLeaveFeedback = { feedbackViewModel.showManualFeedback() }
                    )

                    when (feedbackState.phase) {
                        FeedbackPhase.FEEDBACK_DIALOG -> FeedbackDialog(
                            onSubmit = { rating, comment -> feedbackViewModel.submitFeedback(rating, comment) },
                            onDismiss = { feedbackViewModel.dismissFeedback() }
                        )
                        FeedbackPhase.STORE_REDIRECT -> StoreRedirectDialog(
                            onRateInStore = { feedbackViewModel.openStoreReview() },
                            onDismiss = { feedbackViewModel.declineStoreReview() }
                        )
                        else -> {}
                    }
                }
            }
        }
        }
    }
}

@Composable
private fun AppBottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        BottomNavTab.entries.forEach { tab ->
            val route = bottomNavRoute(tab)
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = { onNavigate(route) },
                icon = { Text(tab.icon) },
                label = {
                    Text(
                        text = bottomNavLabel(tab),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        }
    }
}

@Composable
private fun bottomNavLabel(tab: BottomNavTab): String = when (tab) {
    BottomNavTab.HOME -> stringResource(Res.string.nav_home)
    BottomNavTab.LEARN -> stringResource(Res.string.nav_learn)
    BottomNavTab.EXAM -> stringResource(Res.string.nav_exam)
    BottomNavTab.PROFILE -> stringResource(Res.string.nav_profile)
}

private fun bottomNavRoute(tab: BottomNavTab): String = when (tab) {
    BottomNavTab.HOME -> "home"
    BottomNavTab.LEARN -> "learn"
    BottomNavTab.EXAM -> "exam_intro"
    BottomNavTab.PROFILE -> "profile"
}

@Preview
@Composable
private fun AppBottomNavigationBarPreview() {
    PreviewSurface(contentPadding = PaddingValues()) {
        Scaffold(
            bottomBar = {
                AppBottomNavigationBar(
                    currentRoute = "learn",
                    onNavigate = {}
                )
            }
        ) { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues)) {}
        }
    }
}