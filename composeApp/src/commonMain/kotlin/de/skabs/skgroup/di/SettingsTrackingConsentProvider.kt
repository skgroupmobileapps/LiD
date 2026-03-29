package de.skabs.skgroup.di

import de.skabs.skgroup.data.repository.SettingsRepository
import de.skabs.skgroup.tracking.TrackingConsentProvider

class SettingsTrackingConsentProvider(
    private val settingsRepository: SettingsRepository
) : TrackingConsentProvider {
    override fun isAnalyticsEnabled(): Boolean {
        return settingsRepository.loadSettings().analyticsEnabled
    }
}
