package de.skgroup.einburgerungstest.widget

import android.content.Context
import de.skgroup.einburgerungstest.core.ContextProvider
import de.skgroup.einburgerungstest.core.model.WidgetStats
import de.skgroup.einburgerungstest.data.widget.WidgetDataExporter

/**
 * Android-specific widget synchronization implementation.
 * Uses SharedPreferences for data storage and Glance for widget updates.
 */
actual class WidgetSyncManager {
    
    actual suspend fun syncStats(stats: WidgetStats) {
        val context = ContextProvider.context ?: return
        WidgetUpdater.updateWidgets(context, stats)
    }
    
    actual suspend fun refreshWidgets() {
        val context = ContextProvider.context ?: return
        WidgetUpdater.refreshWidgets(context)
    }
}
