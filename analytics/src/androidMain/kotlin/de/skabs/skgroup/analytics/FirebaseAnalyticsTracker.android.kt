package de.skabs.skgroup.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics

/**
 * Android implementation of [AnalyticsTracker] using Firebase Analytics via GitLive SDK.
 *
 * Requires Firebase to be initialized via google-services.json.
 */
class FirebaseAnalyticsTracker : AnalyticsTracker {
    
    private val firebaseAnalytics = try {
        Firebase.analytics
    } catch (e: Exception) {
        // Firebase not initialized - google-services.json missing
        null
    }
    
    override fun track(event: AnalyticsEvent) {
        val analytics = firebaseAnalytics ?: return
        
        analytics.logEvent(event.name, event.params.mapValues { (_, value) ->
            when (value) {
                is Boolean -> if (value) 1.0 else 0.0
                is Number -> value.toDouble()
                else -> value
            }
        })
    }
    
    override fun setUserProperty(name: String, value: String) {
        firebaseAnalytics?.setUserProperty(name, value)
    }
    
    override fun setUserId(userId: String?) {
        firebaseAnalytics?.setUserId(userId)
    }
}
