package de.skgroup.einburgerungstest.core.util

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual object PlatformUtil {
    actual fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }

    actual fun getPlatformName(): String = "ios"
}
