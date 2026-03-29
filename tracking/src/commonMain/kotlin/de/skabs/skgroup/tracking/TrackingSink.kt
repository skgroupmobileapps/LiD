package de.skabs.skgroup.tracking

interface TrackingSink {
    fun track(event: TrackingEvent)
    fun setUserProperty(name: String, value: String)
    fun setUserId(userId: String?)
}
