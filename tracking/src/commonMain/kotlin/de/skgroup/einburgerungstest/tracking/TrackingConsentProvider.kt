package de.skgroup.einburgerungstest.tracking

interface TrackingConsentProvider {
    fun isAnalyticsEnabled(): Boolean
}

class DisabledByDefaultConsentProvider : TrackingConsentProvider {
    override fun isAnalyticsEnabled(): Boolean = false
}
