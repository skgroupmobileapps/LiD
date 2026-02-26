package de.skabs.skgroup.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import de.skabs.skgroup.core.model.WidgetStats
import de.skabs.skgroup.data.widget.WidgetDataExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Utility object for updating the widget from the main app.
 */
object WidgetUpdater {
    
    /**
     * Update all widget instances with new stats.
     * Call this when user progress changes.
     */
    suspend fun updateWidgets(context: Context, stats: WidgetStats) {
        withContext(Dispatchers.IO) {
            // Export stats to shared preferences
            val exporter = WidgetDataExporter(context)
            exporter.exportStats(stats)
            
            // Refresh all widget instances
            StatsWidget().updateAll(context)
        }
    }
    
    /**
     * Update widgets using the current stats from the repository.
     */
    suspend fun refreshWidgets(context: Context) {
        withContext(Dispatchers.IO) {
            StatsWidget().updateAll(context)
        }
    }
}
