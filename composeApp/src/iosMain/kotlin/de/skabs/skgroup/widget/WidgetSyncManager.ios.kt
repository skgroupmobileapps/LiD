package de.skabs.skgroup.widget

import de.skabs.skgroup.core.model.WidgetStats
import de.skabs.skgroup.data.widget.WidgetDataExporter

/**
 * iOS-specific widget synchronization implementation.
 * Uses UserDefaults with App Group for data storage.
 * WidgetKit will read from the same UserDefaults suite.
 */
actual class WidgetSyncManager {
    private val exporter = WidgetDataExporter()
    
    actual suspend fun syncStats(stats: WidgetStats) {
        // Export stats to shared UserDefaults (App Group)
        // WidgetKit will read this on its timeline refresh
        exporter.exportStats(stats)
        
        // On iOS, WidgetKit handles its own refresh timeline
        // We can't directly trigger a refresh from the app without WidgetCenter API
        // The widget will update on its next timeline refresh
    }
    
    actual suspend fun refreshWidgets() {
        // WidgetKit manages its own refresh cycle
        // Stats are already available via shared UserDefaults
    }
}
