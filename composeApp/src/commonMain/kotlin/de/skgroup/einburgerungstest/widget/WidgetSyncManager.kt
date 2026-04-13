package de.skgroup.einburgerungstest.widget

import de.skgroup.einburgerungstest.core.model.WidgetStats

/**
 * Platform-specific widget synchronization manager.
 * Handles exporting stats and refreshing widget instances.
 */
expect class WidgetSyncManager() {
    /**
     * Sync stats to the widget and trigger a refresh.
     */
    suspend fun syncStats(stats: WidgetStats)
    
    /**
     * Trigger a widget refresh using current stored stats.
     */
    suspend fun refreshWidgets()
}
