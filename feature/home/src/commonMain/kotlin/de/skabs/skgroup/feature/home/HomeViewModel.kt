package de.skabs.skgroup.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.skabs.skgroup.core.model.UserProgress
import de.skabs.skgroup.domain.usecase.ProgressUseCase
import de.skabs.skgroup.domain.usecase.StatisticsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val progress: UserProgress = UserProgress(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val progressUseCase: ProgressUseCase,
    private val statisticsUseCase: StatisticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadProgress()
    }

    fun loadProgress() {
        viewModelScope.launch(Dispatchers.Default) {
            val progress = progressUseCase.getUserProgress()
            _uiState.value = HomeUiState(
                progress = progress,
                isLoading = false
            )
        }
    }
}
