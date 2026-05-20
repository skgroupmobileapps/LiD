package de.skgroup.einburgerungstest.feature.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.skgroup.einburgerungstest.core.util.PlatformUtil
import de.skgroup.einburgerungstest.data.repository.FeedbackRepository
import de.skgroup.einburgerungstest.data.repository.SettingsRepository
import de.skgroup.einburgerungstest.domain.usecase.FeedbackUseCase
import de.skgroup.einburgerungstest.tracking.TrackingClient
import de.skgroup.einburgerungstest.tracking.TrackingEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FeedbackPhase {
    HIDDEN,
    FEEDBACK_DIALOG,
    STORE_REDIRECT,
    THANK_YOU
}

data class FeedbackUiState(
    val phase: FeedbackPhase = FeedbackPhase.HIDDEN,
    val trigger: String = ""
)

class FeedbackViewModel(
    private val feedbackUseCase: FeedbackUseCase,
    private val feedbackRepository: FeedbackRepository,
    private val settingsRepository: SettingsRepository,
    private val trackingClient: TrackingClient
) : ViewModel() {

    companion object {
        private const val APP_VERSION = BuildConfig.APP_VERSION

        // Replace with actual store URLs once published
        private const val GOOGLE_PLAY_URL =
            "https://play.google.com/store/apps/details?id=de.skgroup.einburgerungstest.einbuergerungstest"
        private const val APP_STORE_URL =
            "https://apps.apple.com/app/leben-in-deutschland/id6740043866"
    }

    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    fun checkAndShowFeedbackAfterExam(passed: Boolean) {
        if (feedbackUseCase.shouldShowFeedbackAfterExam(passed)) {
            showDialog("exam_passed")
        }
    }

    fun checkAndShowFeedbackAfterLearning() {
        if (feedbackUseCase.shouldShowFeedbackAfterLearning()) {
            showDialog("learn_session")
        }
    }

    fun showManualFeedback() {
        showDialog("manual")
    }

    fun submitFeedback(rating: Int, comment: String) {
        val trigger = _uiState.value.trigger
        trackingClient.track(TrackingEvent.FeedbackSubmitted(rating, trigger))
        feedbackUseCase.recordFeedbackSubmitted(rating)

        val settings = settingsRepository.loadSettings()
        val platform = PlatformUtil.getPlatformName()

        viewModelScope.launch(Dispatchers.Default) {
            feedbackRepository.submitFeedback(
                rating = rating,
                comment = comment,
                language = settings.language.code,
                federalState = settings.federalState.name,
                appVersion = APP_VERSION,
                platform = platform
            )
        }

        if (feedbackUseCase.isPositiveRating(rating)) {
            _uiState.update { it.copy(phase = FeedbackPhase.STORE_REDIRECT) }
        } else {
            _uiState.update { it.copy(phase = FeedbackPhase.HIDDEN) }
        }
    }

    fun dismissFeedback() {
        val trigger = _uiState.value.trigger
        trackingClient.track(TrackingEvent.FeedbackDismissed(trigger))
        feedbackUseCase.recordFeedbackDismissed()
        _uiState.update { it.copy(phase = FeedbackPhase.HIDDEN) }
    }

    fun openStoreReview() {
        val platform = PlatformUtil.getPlatformName()
        trackingClient.track(TrackingEvent.StoreRedirectAccepted(platform))
        val url = if (platform == "android") GOOGLE_PLAY_URL else APP_STORE_URL
        PlatformUtil.openUrl(url)
        _uiState.update { it.copy(phase = FeedbackPhase.HIDDEN) }
    }

    fun declineStoreReview() {
        trackingClient.track(TrackingEvent.StoreRedirectDeclined)
        _uiState.update { it.copy(phase = FeedbackPhase.HIDDEN) }
    }

    private fun showDialog(trigger: String) {
        trackingClient.track(TrackingEvent.FeedbackDialogShown(trigger))
        _uiState.update {
            it.copy(
                phase = FeedbackPhase.FEEDBACK_DIALOG,
                trigger = trigger
            )
        }
    }
}
