package de.skabs.skgroup.tracking

class DefaultTrackingClient(
    private val consentProvider: TrackingConsentProvider,
    private val sinks: List<TrackingSink>
) : TrackingClient {

    override fun track(event: TrackingEvent) {
        if (!consentProvider.isAnalyticsEnabled()) return
        sinks.forEach { it.track(event) }
    }

    override fun setUserProperty(name: String, value: String) {
        if (!consentProvider.isAnalyticsEnabled()) return
        sinks.forEach { it.setUserProperty(name, value) }
    }

    override fun setUserId(userId: String?) {
        if (!consentProvider.isAnalyticsEnabled()) return
        sinks.forEach { it.setUserId(userId) }
    }
}
