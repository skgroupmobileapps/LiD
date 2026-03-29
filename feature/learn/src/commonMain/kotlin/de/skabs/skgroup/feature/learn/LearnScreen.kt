package de.skabs.skgroup.feature.learn

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.skabs.skgroup.core.model.Topic
import de.skabs.skgroup.core.util.BackHandler
import de.skabs.skgroup.designsystem.components.*
import de.skabs.skgroup.designsystem.theme.*
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun LearnScreen(
    viewModel: LearnViewModel,
    onTopicSelected: (Topic) -> Unit = {},
    onBookmarksClick: () -> Unit = {},
    onAllQuestionsClick: () -> Unit = {},
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LearnScreenContent(
        uiState = uiState,
        onTopicSelected = onTopicSelected,
        onBookmarksClick = onBookmarksClick,
        onAllQuestionsClick = onAllQuestionsClick,
        modifier = modifier
    )
}

@Composable
fun LearnScreenContent(
    uiState: LearnUiState,
    onTopicSelected: (Topic) -> Unit = {},
    onBookmarksClick: () -> Unit = {},
    onAllQuestionsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // Header
        Text(
            text = stringResource(Res.string.learn_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(Res.string.learn_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onBookmarksClick,
                shape = de.skabs.skgroup.designsystem.theme.ButtonShape,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(stringResource(Res.string.learn_bookmarks_count, uiState.bookmarkCount))
            }
            OutlinedButton(
                onClick = onAllQuestionsClick,
                shape = de.skabs.skgroup.designsystem.theme.ButtonShape,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(stringResource(Res.string.learn_all_questions))
            }
        }

        Spacer(Modifier.height(20.dp))

        // Topics header
        Text(
            text = stringResource(Res.string.learn_topics),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        // Topic cards
        val topicColors = mapOf(
            Topic.DEMOCRACY_AND_STATE to TopicDemocracy,
            Topic.RIGHTS_AND_DUTIES to TopicRights,
            Topic.HISTORY to TopicHistory,
            Topic.SOCIETY_AND_CULTURE to TopicSociety,
            Topic.SYMBOLS_AND_GEOGRAPHY to TopicSymbols,
            Topic.FEDERAL_STATE to TopicState
        )

        uiState.topicProgressList.forEach { topicProgress ->
            TopicCard(
                title = topicProgress.topic.displayName,
                description = topicProgress.topic.description,
                icon = topicProgress.topic.icon,
                accentColor = topicColors[topicProgress.topic] ?: PrimaryGreen,
                progress = topicProgress.progressPercent / 100f,
                answeredCount = topicProgress.answeredCorrectly,
                totalCount = topicProgress.totalQuestions,
                onClick = { onTopicSelected(topicProgress.topic) }
            )
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(24.dp))
    }
}


