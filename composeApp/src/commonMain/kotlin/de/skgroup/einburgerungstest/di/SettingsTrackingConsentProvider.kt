package de.skgroup.einburgerungstest.di

import de.skgroup.einburgerungstest.data.repository.SettingsRepository
import de.skgroup.einburgerungstest.tracking.TrackingConsentProvider

class SettingsTrackingConsentProvider(
    private val settingsRepository: SettingsRepository
) : TrackingConsentProvider {
    override fun isAnalyticsEnabled(): Boolean {
        return settingsRepository.loadSettings().analyticsEnabled
    }
}
