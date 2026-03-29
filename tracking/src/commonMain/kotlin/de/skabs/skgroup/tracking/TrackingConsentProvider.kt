package de.skabs.skgroup.tracking

interface TrackingConsentProvider {
    fun isAnalyticsEnabled(): Boolean
}

class DisabledByDefaultConsentProvider : TrackingConsentProvider {
    override fun isAnalyticsEnabled(): Boolean = false
}
