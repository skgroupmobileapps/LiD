package de.skabs.skgroup.core

import android.content.Context

/**
 * Singleton to provide Android [Context] to platform-specific utilities.
 *
 * Initialize via [init] in your Application class's onCreate().
 */
object ContextProvider {
    var context: Context? = null
        private set

    fun init(context: Context) {
        this.context = context.applicationContext
    }
}
