package de.skabs.skgroup.core.model

import kotlinx.serialization.Serializable

/**
 * Statistics data shared with home screen widgets.
 */
@Serializable
data class WidgetStats(
    val correctAnswers: Int = 0,
    val accuracyPercent: Float = 0f,
    val dayStreak: Int = 0
)
