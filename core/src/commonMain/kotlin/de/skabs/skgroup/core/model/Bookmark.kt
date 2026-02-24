package de.skabs.skgroup.core.model

import kotlinx.serialization.Serializable

/**
 * A bookmarked question.
 */
@Serializable
data class Bookmark(
    val questionId: Int,
    val timestampMs: Long
)
