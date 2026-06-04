package de.skgroup.einburgerungstest.navigation

/**
 * Encodes/decodes review question IDs passed through navigation routes.
 *
 * Underscore is used as canonical delimiter because it is route-safe and avoids
 * URI encoding edge cases that may collapse comma-separated payloads.
 */
fun encodeReviewQuestionIds(ids: List<Int>): String {
    return ids.joinToString("_")
}

fun decodeReviewQuestionIds(raw: String?): List<Int> {
    if (raw.isNullOrBlank()) return emptyList()

    val cleaned = raw
        .trim()
        .trim('"')
        .removePrefix("[")
        .removeSuffix("]")
        .removePrefix("{")
        .removeSuffix("}")

    val separator = when {
        cleaned.contains('_') -> '_'
        cleaned.contains(',') -> ','
        cleaned.contains('|') -> '|'
        else -> null
    }

    val parts = if (separator == null) listOf(cleaned) else cleaned.split(separator)
    return parts.mapNotNull { part -> part.trim().toIntOrNull() }
}
