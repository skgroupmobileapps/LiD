package de.skgroup.einburgerungstest.core.model

import kotlinx.serialization.Serializable

/**
 * A bookmarked question.
 */
@Serializable
data class Bookmark(
    val questionId: Int,
    val timestampMs: Long
)
