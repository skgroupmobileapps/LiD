package de.skabs.skgroup

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    val settingsRepository: SettingsRepository = koinInject()
    val questionSeeder: QuestionSeeder = koinInject()

    // Seed questions on first launch
    LaunchedEffect(Unit) {
        questionSeeder.seedIfNeeded()
    }

    val hasCompletedOnboarding = remember { settingsRepository.hasCompletedOnboarding() }
    val startDestination = if (hasCompletedOnboarding) "home" else "onboarding"

    EinbuergerungTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

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
                            NavigationBarItem(
                                selected = currentRoute == route,
                                onClick = {
                                    navController.navigate(route) {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Text(tab.icon) },
                                label = { Text(tab.label, style = MaterialTheme.typography.labelSmall) }
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
                            val settings = settingsRepository.loadSettings().copy(
                                language = language,
                                federalState = state,
                                hasCompletedOnboarding = true
                            )
                            settingsRepository.saveSettings(settings)
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
                        onContinueLearning = { navController.navigate("learn_question/ALL/NONE") },
                        onExamMode = { navController.navigate("exam_intro") },
                        onByTopic = { navController.navigate("learn") },
                        onBookmarks = { navController.navigate("learn") },
                        onAllQuestions = { navController.navigate("learn_question/ALL/NONE") }
                    )
                }

                composable("learn") {
                    val viewModel: LearnViewModel = koinViewModel()
                    LearnScreen(
                        viewModel = viewModel,
                        onTopicSelected = { topic ->
                            navController.navigate("learn_question/TOPIC/${topic.name}")
                        },
                        onBookmarksClick = { /* Navigate to bookmarked questions */ },
                        onAllQuestionsClick = {
                            navController.navigate("learn_question/ALL/NONE")
                        }
                    )
                }

                composable(
                    route = "learn_question/{mode}/{topicId}",
                ) { backStackEntry ->
                    val mode = backStackEntry.arguments?.getString("mode") ?: "ALL"
                    val topicId = backStackEntry.arguments?.getString("topicId")
                    
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
                        onBack = { navController.popBackStack() }
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
                        ExamPhase.IN_PROGRESS -> ExamQuestionScreen(viewModel = viewModel)
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