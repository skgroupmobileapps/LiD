package de.skgroup.einburgerungstest.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.UserProgress
import de.skgroup.einburgerungstest.data.repository.SettingsRepository
import de.skgroup.einburgerungstest.domain.usecase.BookmarkUseCase
import de.skgroup.einburgerungstest.domain.usecase.ProgressUseCase
import de.skgroup.einburgerungstest.domain.usecase.StatisticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val progress: UserProgress = UserProgress(),
    val isLoading: Boolean = true,
)

class HomeViewModel(
    private val progressUseCase: ProgressUseCase,
    private val statisticsUseCase: StatisticsUseCase,
    private val settingsRepository: SettingsRepository,
    private val bookmarkUseCase: BookmarkUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val federalState: FederalState
        get() = settingsRepository.loadSettings().federalState

    init {
        loadProgress()
    }

    fun loadProgress() {
        viewModelScope.launch(Dispatchers.Default) {
            val progress = progressUseCase.getUserProgressForState(federalState)
            _uiState.value = HomeUiState(
                progress = progress,
                isLoading = false,
            )
        }
    }
}
