package de.skabs.skgroup.core.navigation

/**
 * Deeplink constants for navigating into the app from widgets or external sources.
 */
object Deeplinks {
    const val SCHEME = "kmpexam"
    
    const val HOME = "$SCHEME://home"
    const val LEARN = "$SCHEME://learn"
    const val EXAM = "$SCHEME://exam"
    
    /**
     * Parse a deeplink URL and return the destination route, or null if invalid.
     */
    fun parseRoute(url: String): String? {
        return when {
            url.startsWith("$SCHEME://home") -> "home"
            url.startsWith("$SCHEME://learn") -> "learn"
            url.startsWith("$SCHEME://exam") -> "exam_intro"
            else -> null
        }
    }
}
