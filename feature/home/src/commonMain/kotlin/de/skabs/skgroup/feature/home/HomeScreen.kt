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
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource


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
                text = stringResource(Res.string.home_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(Res.string.home_subtitle),
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
                    label = stringResource(Res.string.home_questions),
                    iconBackground = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = "📈",
                    value = "${progress.accuracy.toInt()}%",
                    label = stringResource(Res.string.home_accuracy),
                    iconBackground = AccentGold,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = "🔥",
                    value = "${progress.dayStreak}",
                    label = stringResource(Res.string.home_day_streak),
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
                            stringResource(Res.string.home_overall_progress),
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
                        stringResource(Res.string.home_questions_completed, progress.totalCorrect, progress.totalQuestionsAvailable),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Quick Actions
            Text(
                stringResource(Res.string.home_quick_actions),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            // Continue Learning (primary)
            QuickActionCardPrimary(
                title = stringResource(Res.string.home_continue_learning),
                subtitle = stringResource(Res.string.home_continue_subtitle),
                onClick = onContinueLearning
            )

            Spacer(Modifier.height(12.dp))

            // Grid of secondary actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCardSecondary(
                    title = stringResource(Res.string.home_exam_mode),
                    subtitle = stringResource(Res.string.home_exam_subtitle),
                    icon = "🎓",
                    iconColor = AccentPurple,
                    onClick = onExamMode,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCardSecondary(
                    title = stringResource(Res.string.home_by_topic),
                    subtitle = stringResource(Res.string.home_by_topic_subtitle),
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
                    title = stringResource(Res.string.home_bookmarks),
                    subtitle = stringResource(Res.string.home_bookmarks_saved, progress.bookmarkCount),
                    icon = "🔖",
                    iconColor = AccentGold,
                    onClick = onBookmarks,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCardSecondary(
                    title = stringResource(Res.string.home_all_questions),
                    subtitle = stringResource(Res.string.home_all_questions_subtitle),
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


