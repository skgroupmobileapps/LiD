package de.skabs.skgroup.data.widget

import de.skabs.skgroup.core.model.WidgetStats

/**
 * Platform-specific interface for exporting widget statistics to shared storage.
 * 
 * Android: Uses SharedPreferences
 * iOS: Uses UserDefaults with App Group
 */
expect class WidgetDataExporter {
    /**
     * Export stats to shared storage accessible by the widget.
     */
    fun exportStats(stats: WidgetStats)
    
    /**
     * Read stats from shared storage.
     */
    fun readStats(): WidgetStats
}

/**
 * Keys used for widget data storage.
 */
object WidgetDataKeys {
    const val PREFS_NAME = "widget_stats"
    const val KEY_CORRECT = "widget_correct"
    const val KEY_ACCURACY = "widget_accuracy"
    const val KEY_STREAK = "widget_streak"
    
    // iOS App Group identifier
    const val IOS_APP_GROUP = "group.de.skabs.skgroup.kmpexam"
}
