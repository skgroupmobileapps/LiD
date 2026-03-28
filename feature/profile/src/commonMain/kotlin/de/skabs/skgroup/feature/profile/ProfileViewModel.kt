package de.skabs.skgroup.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.skabs.skgroup.core.model.ExamHistoryEntry
import de.skabs.skgroup.core.model.UserProgress
import de.skabs.skgroup.core.model.UserSettings
import de.skabs.skgroup.data.repository.ExamRepository
import de.skabs.skgroup.data.repository.SettingsRepository
import de.skabs.skgroup.domain.usecase.ExamStats
import de.skabs.skgroup.domain.usecase.ProgressUseCase
import de.skabs.skgroup.domain.usecase.StatisticsUseCase
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
    private val examRepository: ExamRepository
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
            settingsRepository.saveSettings(settings)
            _uiState.update { it.copy(settings = settings) }
        }
    }
    
    fun resetStatistics() {
        viewModelScope.launch(Dispatchers.Default) {
            statisticsUseCase.resetStatistics()
            loadProfile()
        }
    }
}
