package de.skabs.skgroup.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.skabs.skgroup.core.model.UserProgress
import de.skabs.skgroup.designsystem.components.AppProgressBar
import de.skabs.skgroup.designsystem.components.GermanFlagBar
import de.skabs.skgroup.designsystem.components.QuickActionCardPrimary
import de.skabs.skgroup.designsystem.components.QuickActionCardSecondary
import de.skabs.skgroup.designsystem.components.StatCard
import de.skabs.skgroup.designsystem.theme.AccentGold
import de.skabs.skgroup.designsystem.theme.AccentOrange
import de.skabs.skgroup.designsystem.theme.AccentPink
import de.skabs.skgroup.designsystem.theme.AccentPurple
import de.skabs.skgroup.designsystem.theme.PreviewSurface
import de.skabs.skgroup.designsystem.theme.PrimaryGreen
import de.skabs.skgroup.designsystem.theme.QuizCardShape
import de.skabs.skgroup.designsystem.theme.SuccessGreen
import kmpexam.resources.generated.resources.Res
import kmpexam.resources.generated.resources.home_accuracy
import kmpexam.resources.generated.resources.home_all_questions
import kmpexam.resources.generated.resources.home_all_questions_subtitle
import kmpexam.resources.generated.resources.home_bookmarks
import kmpexam.resources.generated.resources.home_bookmarks_saved
import kmpexam.resources.generated.resources.home_by_topic
import kmpexam.resources.generated.resources.home_by_topic_subtitle
import kmpexam.resources.generated.resources.home_continue_learning
import kmpexam.resources.generated.resources.home_continue_subtitle
import kmpexam.resources.generated.resources.home_day_streak
import kmpexam.resources.generated.resources.home_exam_mode
import kmpexam.resources.generated.resources.home_exam_subtitle
import kmpexam.resources.generated.resources.home_overall_progress
import kmpexam.resources.generated.resources.home_progress
import kmpexam.resources.generated.resources.home_questions
import kmpexam.resources.generated.resources.home_questions_completed
import kmpexam.resources.generated.resources.home_quick_actions
import kmpexam.resources.generated.resources.home_subtitle
import kmpexam.resources.generated.resources.home_title
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

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.loadProgress()
    }

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
        GermanFlagBar()

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(16.dp))

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

            HomeOverallProgressCard(progress = progress)

            Spacer(Modifier.height(24.dp))

            HomeQuickActionsSection(
                progress = progress,
                onContinueLearning = onContinueLearning,
                onExamMode = onExamMode,
                onByTopic = onByTopic,
                onBookmarks = onBookmarks,
                onAllQuestions = onAllQuestions
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HomeOverallProgressCard(progress: UserProgress) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = QuizCardShape,
        colors = CardDefaults.cardColors(
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
                    text = stringResource(Res.string.home_overall_progress),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${progress.overallProgressPercent.toInt()}%",
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
                text = stringResource(
                    Res.string.home_questions_completed,
                    progress.totalCorrect,
                    progress.totalQuestionsAvailable
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HomeQuickActionsSection(
    progress: UserProgress,
    onContinueLearning: () -> Unit,
    onExamMode: () -> Unit,
    onByTopic: () -> Unit,
    onBookmarks: () -> Unit,
    onAllQuestions: () -> Unit
) {
    Text(
        text = stringResource(Res.string.home_quick_actions),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(12.dp))

    QuickActionCardPrimary(
        title = stringResource(Res.string.home_continue_learning),
        subtitle = stringResource(Res.string.home_continue_subtitle),
        onClick = onContinueLearning
    )

    Spacer(Modifier.height(12.dp))

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
}

@Preview
@Composable
private fun HomeScreenContentPreview() {
    PreviewSurface {
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

private fun previewHomeProgress() = UserProgress(
    totalAnswered = 128,
    totalCorrect = 96,
    accuracy = 75f,
    bookmarkCount = 14,
    dayStreak = 9,
    overallProgressPercent = 41.3f,
    totalQuestionsAvailable = 310
)


