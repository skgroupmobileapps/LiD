package de.skabs.skgroup.tracking

interface TrackingClient {
    fun track(event: TrackingEvent)
    fun setUserProperty(name: String, value: String)
    fun setUserId(userId: String?)
}
