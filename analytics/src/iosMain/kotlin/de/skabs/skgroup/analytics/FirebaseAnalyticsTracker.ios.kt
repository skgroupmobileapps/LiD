package de.skabs.skgroup.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import de.skabs.skgroup.tracking.TrackingEvent
import de.skabs.skgroup.tracking.TrackingSink

/**
 * iOS implementation of [TrackingSink] using Firebase Analytics via GitLive SDK.
 *
 * Requires Firebase to be initialized via GoogleService-Info.plist.
 */
class FirebaseAnalyticsSink : TrackingSink {
    
    private val firebaseAnalytics = try {
        Firebase.analytics
    } catch (e: Exception) {
        // Firebase not initialized - GoogleService-Info.plist missing
        null
    }
    
    override fun track(event: TrackingEvent) {
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
