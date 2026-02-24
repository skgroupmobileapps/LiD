package de.skabs.skgroup.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.skabs.skgroup.designsystem.components.*
import de.skabs.skgroup.designsystem.theme.*


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onContinueLearning: () -> Unit = {},
    onExamMode: () -> Unit = {},
    onByTopic: () -> Unit = {},
    onBookmarks: () -> Unit = {},
    onAllQuestions: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    HomeScreenContent(
        uiState = uiState,
        onContinueLearning = onContinueLearning,
        onExamMode = onExamMode,
        onByTopic = onByTopic,
        onBookmarks = onBookmarks,
        onAllQuestions = onAllQuestions,
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onContinueLearning: () -> Unit = {},
    onExamMode: () -> Unit = {},
    onByTopic: () -> Unit = {},
    onBookmarks: () -> Unit = {},
    onAllQuestions: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val progress = uiState.progress

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // German flag bar at top
        GermanFlagBar()

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(16.dp))

            // Title
            Text(
                text = "Leben in Deutschland",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Your path to German citizenship",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(20.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    icon = "🎯",
                    value = "${progress.totalCorrect}/${progress.totalQuestionsAvailable}",
                    label = "Questions",
                    iconBackground = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = "📈",
                    value = "${progress.accuracy.toInt()}%",
                    label = "Accuracy",
                    iconBackground = AccentGold,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = "🔥",
                    value = "${progress.dayStreak}",
                    label = "Day Streak",
                    iconBackground = AccentPink,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Overall progress
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth(),
                shape = de.skabs.skgroup.designsystem.theme.QuizCardShape,
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Overall Progress",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "${progress.overallProgressPercent.toInt()}%",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    AppProgressBar(
                        progress = progress.overallProgressPercent / 100f,
                        color = PrimaryGreen
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "${progress.totalCorrect} of ${progress.totalQuestionsAvailable} questions completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Quick Actions
            Text(
                "Quick Actions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            // Continue Learning (primary)
            QuickActionCardPrimary(
                title = "Continue Learning",
                subtitle = "Pick up where you left off",
                onClick = onContinueLearning
            )

            Spacer(Modifier.height(12.dp))

            // Grid of secondary actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCardSecondary(
                    title = "Exam Mode",
                    subtitle = "Simulate the test",
                    icon = "🎓",
                    iconColor = AccentPurple,
                    onClick = onExamMode,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCardSecondary(
                    title = "By Topic",
                    subtitle = "Study categories",
                    icon = "📊",
                    iconColor = AccentOrange,
                    onClick = onByTopic,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCardSecondary(
                    title = "Bookmarks",
                    subtitle = "${progress.bookmarkCount} saved",
                    icon = "🔖",
                    iconColor = AccentGold,
                    onClick = onBookmarks,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCardSecondary(
                    title = "All 300",
                    subtitle = "Browse all",
                    icon = "📚",
                    iconColor = PrimaryGreen,
                    onClick = onAllQuestions,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}


