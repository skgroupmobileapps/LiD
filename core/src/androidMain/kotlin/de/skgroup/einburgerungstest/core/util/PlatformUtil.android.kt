package de.skgroup.einburgerungstest.core.util

import android.content.Intent
import android.net.Uri
import de.skgroup.einburgerungstest.core.ContextProvider

actual object PlatformUtil {
    actual fun openUrl(url: String) {
        val context = ContextProvider.context ?: return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    actual fun getPlatformName(): String = "android"
}
