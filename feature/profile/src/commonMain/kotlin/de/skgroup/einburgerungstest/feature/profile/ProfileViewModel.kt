package de.skgroup.einburgerungstest.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.skgroup.einburgerungstest.core.model.ExamHistoryEntry
import de.skgroup.einburgerungstest.core.model.UserProgress
import de.skgroup.einburgerungstest.core.model.UserSettings
import de.skgroup.einburgerungstest.data.repository.ExamRepository
import de.skgroup.einburgerungstest.data.repository.SettingsRepository
import de.skgroup.einburgerungstest.domain.usecase.ExamStats
import de.skgroup.einburgerungstest.domain.usecase.ProgressUseCase
import de.skgroup.einburgerungstest.domain.usecase.StatisticsUseCase
import de.skgroup.einburgerungstest.tracking.TrackingClient
import de.skgroup.einburgerungstest.tracking.TrackingEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val settings: UserSettings = UserSettings(),
    val progress: UserProgress = UserProgress(),
    val examStats: ExamStats = ExamStats(),
    val examHistory: List<ExamHistoryEntry> = emptyList(),
    val isLoading: Boolean = true
)

class ProfileViewModel(
    private val progressUseCase: ProgressUseCase,
    private val statisticsUseCase: StatisticsUseCase,
    private val settingsRepository: SettingsRepository,
    private val examRepository: ExamRepository,
    private val trackingClient: TrackingClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch(Dispatchers.Default) {
            val settings = settingsRepository.loadSettings()
            val progress = progressUseCase.getUserProgressForState(settings.federalState)
            val examStats = statisticsUseCase.getExamStats()
            val examHistory = examRepository.getExamHistory()

            _uiState.update {
                ProfileUiState(
                    settings = settings,
                    progress = progress,
                    examStats = examStats,
                    examHistory = examHistory,
                    isLoading = false
                )
            }
        }
    }

    fun updateSettings(settings: UserSettings) {
        viewModelScope.launch(Dispatchers.Default) {
            val previous = _uiState.value.settings

            if (previous.analyticsEnabled && !settings.analyticsEnabled) {
                trackingClient.track(TrackingEvent.ConsentChanged(analyticsEnabled = false))
            }

            settingsRepository.saveSettings(settings)
            _uiState.update { it.copy(settings = settings) }

            if (previous.language != settings.language) {
                trackingClient.track(TrackingEvent.LanguageChanged(settings.language.code))
            }
            if (previous.themeMode != settings.themeMode) {
                trackingClient.track(TrackingEvent.ThemeChanged(settings.themeMode.name))
            }
            if (!previous.analyticsEnabled && settings.analyticsEnabled) {
                trackingClient.track(TrackingEvent.ConsentChanged(analyticsEnabled = true))
            }
        }
    }
    
    fun resetStatistics() {
        viewModelScope.launch(Dispatchers.Default) {
            statisticsUseCase.resetStatistics()
            trackingClient.track(TrackingEvent.SettingsReset)
            loadProfile()
        }
    }
}
