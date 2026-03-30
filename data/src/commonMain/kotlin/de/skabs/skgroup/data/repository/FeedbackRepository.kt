package de.skabs.skgroup.data.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class FeedbackRepository {

    private val firestore by lazy {
        try {
            Firebase.firestore
        } catch (_: Exception) {
            null
        }
    }

    suspend fun submitFeedback(
        rating: Int,
        comment: String,
        language: String,
        federalState: String,
        appVersion: String,
        platform: String
    ) {
        try {
            val data = hashMapOf(
                "rating" to rating,
                "comment" to comment,
                "language" to language,
                "federalState" to federalState,
                "appVersion" to appVersion,
                "platform" to platform,
                "timestampMs" to kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
            )
            firestore?.collection("feedback")?.add(data)
        } catch (_: Exception) {
            // Silently fail — don't block user experience for remote persistence
        }
    }
}
