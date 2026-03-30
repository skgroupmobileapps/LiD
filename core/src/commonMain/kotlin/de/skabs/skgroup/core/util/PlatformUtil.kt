package de.skabs.skgroup.core.util

/**
 * Platform-specific utilities.
 */
expect object PlatformUtil {
    /**
     * Opens a URL in the system browser or handles mailto: links.
     */
    fun openUrl(url: String)

    /**
     * Returns the platform name: "android" or "ios".
     */
    fun getPlatformName(): String
}
