package de.skgroup.einburgerungstest.feature.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import de.skgroup.einburgerungstest.core.model.ExamHistoryEntry
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Language
import de.skgroup.einburgerungstest.core.model.UserProgress
import de.skgroup.einburgerungstest.core.model.UserSettings
import de.skgroup.einburgerungstest.core.util.PlatformUtil
import de.skgroup.einburgerungstest.designsystem.components.*
import de.skgroup.einburgerungstest.designsystem.theme.*
import de.skgroup.einburgerungstest.domain.usecase.ExamStats
import kmpexam.resources.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview

// App metadata constants
private const val APP_VERSION = "0.8.3"
private const val CATALOGUE_DATE = "Mai 2025(Latest)"
private const val CONTACT_EMAIL = "skgroup.mobileapps@gmail.com"

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLeaveFeedback: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    ProfileScreenContent(
        uiState = uiState,
        onUpdateSettings = { viewModel.updateSettings(it) },
        onResetStatistics = { viewModel.resetStatistics() },
        onLeaveFeedback = onLeaveFeedback,
        modifier = modifier
    )
}

@Composable
fun ProfileScreenContent(
    uiState: ProfileUiState,
    onUpdateSettings: (UserSettings) -> Unit = {},
    onResetStatistics: () -> Unit = {},
    onLeaveFeedback: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val settings = uiState.settings
    val progress = uiState.progress
    val examStats = uiState.examStats
    
    // Language picker dialog state
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    
    // Language picker dialog
    if (showLanguageDialog) {
        LanguagePickerDialog(
            currentLanguage = settings.language,
            onLanguageSelected = { language ->
                onUpdateSettings(settings.copy(language = language))
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showResetDialog) {
        ConfirmationDialog(
            title = stringResource(Res.string.dialog_reset_stats_title),
            message = stringResource(Res.string.dialog_reset_stats_message),
            confirmText = stringResource(Res.string.profile_reset),
            dismissText = stringResource(Res.string.dialog_cancel),
            onConfirm = {
                showResetDialog = false
                onResetStatistics()
            },
            onDismiss = { showResetDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.profile_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // Profile card
        ProfileIdentityCard(settings = settings)

        Spacer(Modifier.height(20.dp))

        // Stats
        Text(stringResource(Res.string.profile_progress), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                icon = "🎯",
                value = "${progress.totalCorrect}",
                label = stringResource(Res.string.profile_correct),
                iconBackground = SuccessGreen,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = "📈",
                value = "${progress.accuracy.toInt()}%",
                label = stringResource(Res.string.profile_accuracy),
                iconBackground = AccentGold,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = "🔥",
                value = "${progress.dayStreak}",
                label = stringResource(Res.string.profile_streak),
                iconBackground = AccentPink,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Exam history summary
        Text(stringResource(Res.string.profile_exams), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        ProfileExamStatsCard(examStats = examStats)

        Spacer(Modifier.height(20.dp))

        // Recent exam history
        if (uiState.examHistory.isNotEmpty()) {
            Text(stringResource(Res.string.profile_exam_history), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))

            RecentExamHistorySection(examHistory = uiState.examHistory)
        }

        Spacer(Modifier.height(20.dp))

        // Settings Section
        Text(stringResource(Res.string.profile_settings), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        ProfileSettingsCard(
            settings = settings,
            onOpenLanguageDialog = { showLanguageDialog = true },
            onUpdateSettings = onUpdateSettings,
            onResetStatistics = { showResetDialog = true },
            onLeaveFeedback = onLeaveFeedback
        )

        Spacer(Modifier.height(20.dp))

        // About Section
        Text(stringResource(Res.string.profile_about), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        ProfileAboutCard()

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileIdentityCard(settings: UserSettings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = PrimaryGreen.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("👤", style = MaterialTheme.typography.headlineMedium)
                }
            }
            Column {
                Text(
                    text = if (settings.isGuest) stringResource(Res.string.profile_guest) else stringResource(Res.string.profile_student),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${settings.federalState.displayName} • ${settings.language.displayName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProfileExamStatsCard(examStats: ExamStats) {
    val isDark = isSystemInDarkTheme()
    // SuccessGreen (#38A169) on dark surface = 2.18:1 (failing); SuccessGreenTextDark (#68D391) = 7.3:1
    val successTextColor = if (isDark) SuccessGreenTextDark else SuccessGreen
    // AccentGold (#FFB800) on white = 1.70:1 (failing); AccentGoldDark (#B7860B) on white = 4.74:1
    // AccentGold on dark is fine (9.95:1), so only swap in light mode
    val goldTextColor = if (isDark) AccentGold else AccentGoldDark
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(stringResource(Res.string.profile_attempts), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${examStats.totalAttempts}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(stringResource(Res.string.profile_passed), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${examStats.totalPassed}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = successTextColor)
                }
                Column {
                    Text(stringResource(Res.string.profile_avg_score), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${examStats.averageScore.toInt()}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = goldTextColor)
                }
            }
        }
    }
}

@Composable
private fun RecentExamHistorySection(examHistory: List<ExamHistoryEntry>) {
    val isDark = isSystemInDarkTheme()
    val passColor = if (isDark) SuccessGreenTextDark else SuccessGreen
    val failColor = if (isDark) androidx.compose.ui.graphics.Color(0xFFFF8A80) else ErrorRed
    examHistory.take(5).forEach { entry ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(if (entry.passed) "✅" else "❌")
                    Text(
                        "${entry.correctCount}/${entry.totalQuestions}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    "${entry.scorePercent.toInt()}%",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (entry.passed) passColor else failColor
                )
            }
        }
    }
}

@Composable
private fun ProfileSettingsCard(
    settings: UserSettings,
    onOpenLanguageDialog: () -> Unit,
    onUpdateSettings: (UserSettings) -> Unit,
    onResetStatistics: () -> Unit,
    onLeaveFeedback: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenLanguageDialog() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(Res.string.profile_app_language), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(stringResource(Res.string.profile_app_language_subtitle), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    text = settings.language.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(Res.string.profile_dark_mode), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(stringResource(Res.string.profile_dark_mode_subtitle), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = settings.darkMode,
                    onCheckedChange = { isChecked ->
                        onUpdateSettings(settings.copy(darkMode = isChecked))
                    }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(Res.string.profile_analytics), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(stringResource(Res.string.profile_analytics_subtitle), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = settings.analyticsEnabled,
                    onCheckedChange = { isChecked ->
                        onUpdateSettings(settings.copy(analyticsEnabled = isChecked))
                    }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLeaveFeedback() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(Res.string.profile_leave_feedback), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(stringResource(Res.string.profile_leave_feedback_subtitle), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("💬", style = MaterialTheme.typography.titleLarge)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(Res.string.profile_reset_stats), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(stringResource(Res.string.profile_reset_stats_subtitle), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Button(
                    onClick = onResetStatistics,
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed.copy(alpha = 0.1f), contentColor = ErrorRed),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(stringResource(Res.string.profile_reset))
                }
            }
        }
    }
}

@Composable
private fun ProfileAboutCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(Res.string.profile_app_version), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(APP_VERSION, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(Res.string.profile_catalogue_date), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(CATALOGUE_DATE, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(Res.string.profile_contact), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = CONTACT_EMAIL,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        PlatformUtil.openUrl("mailto:$CONTACT_EMAIL")
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenContentPreview() {
    PreviewSurface {
        ProfileScreenContent(uiState = previewProfileUiState())
    }
}

@Preview
@Composable
private fun ProfileScreenContentDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ProfileScreenContent(uiState = previewProfileUiState())
    }
}

@Preview
@Composable
private fun ProfileSettingsCardPreview() {
    PreviewSurface {
        ProfileSettingsCard(
            settings = previewProfileUiState().settings,
            onOpenLanguageDialog = {},
            onUpdateSettings = {},
            onResetStatistics = {}
        )
    }
}

@Preview
@Composable
private fun ProfileSettingsCardDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ProfileSettingsCard(
            settings = previewProfileUiState().settings,
            onOpenLanguageDialog = {},
            onUpdateSettings = {},
            onResetStatistics = {}
        )
    }
}

@Preview
@Composable
private fun ProfileAboutCardPreview() {
    PreviewSurface {
        ProfileAboutCard()
    }
}

@Preview
@Composable
private fun ProfileAboutCardDarkPreview() {
    PreviewSurface(darkTheme = true) {
        ProfileAboutCard()
    }
}

private fun previewProfileUiState() = ProfileUiState(
    settings = UserSettings(
        language = Language.ENGLISH,
        federalState = FederalState.HAMBURG,
        darkMode = false,
        analyticsEnabled = true,
        hasCompletedOnboarding = true,
        isGuest = true
    ),
    progress = UserProgress(
        totalAnswered = 142,
        totalCorrect = 103,
        accuracy = 72.5f,
        bookmarkCount = 12,
        dayStreak = 11,
        overallProgressPercent = 46.1f,
        totalQuestionsAvailable = 310
    ),
    examStats = ExamStats(
        totalAttempts = 8,
        totalPassed = 6,
        averageScore = 74.8f
    ),
    examHistory = listOf(
        ExamHistoryEntry(
            id = 1,
            totalQuestions = 33,
            correctCount = 25,
            passed = true,
            scorePercent = 75.8f,
            timestampMs = 0L,
            federalState = FederalState.HAMBURG
        ),
        ExamHistoryEntry(
            id = 2,
            totalQuestions = 33,
            correctCount = 16,
            passed = false,
            scorePercent = 48.5f,
            timestampMs = 0L,
            federalState = FederalState.HAMBURG
        )
    ),
    isLoading = false
)