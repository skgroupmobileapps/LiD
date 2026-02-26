package de.skabs.skgroup.analytics

/**
 * Interface for analytics tracking.
 *
 * Platform-specific implementations use Firebase Analytics.
 */
interface AnalyticsTracker {
    
    /**
     * Track an analytics event.
     */
    fun track(event: AnalyticsEvent)
    
    /**
     * Set user property.
     */
    fun setUserProperty(name: String, value: String)
    
    /**
     * Set user ID for tracking (optional, for logged-in users).
     */
    fun setUserId(userId: String?)
}
