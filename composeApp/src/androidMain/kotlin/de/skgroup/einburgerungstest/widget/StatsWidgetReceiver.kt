package de.skgroup.einburgerungstest.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * Broadcast receiver for the stats widget.
 * Handles widget lifecycle events and triggers updates.
 */
class StatsWidgetReceiver : GlanceAppWidgetReceiver() {
    
    override val glanceAppWidget: GlanceAppWidget = StatsWidget()
}
