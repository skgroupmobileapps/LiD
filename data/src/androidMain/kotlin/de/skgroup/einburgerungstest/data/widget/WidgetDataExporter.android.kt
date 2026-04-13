package de.skgroup.einburgerungstest.data.widget

import android.content.Context
import de.skgroup.einburgerungstest.core.model.WidgetStats

/**
 * Android implementation using SharedPreferences.
 */
actual class WidgetDataExporter(
    private val context: Context
) {
    private val prefs by lazy {
        context.getSharedPreferences(WidgetDataKeys.PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    actual fun exportStats(stats: WidgetStats) {
        prefs.edit().apply {
            putInt(WidgetDataKeys.KEY_CORRECT, stats.correctAnswers)
            putFloat(WidgetDataKeys.KEY_ACCURACY, stats.accuracyPercent)
            putInt(WidgetDataKeys.KEY_STREAK, stats.dayStreak)
            apply()
        }
    }
    
    actual fun readStats(): WidgetStats {
        return WidgetStats(
            correctAnswers = prefs.getInt(WidgetDataKeys.KEY_CORRECT, 0),
            accuracyPercent = prefs.getFloat(WidgetDataKeys.KEY_ACCURACY, 0f),
            dayStreak = prefs.getInt(WidgetDataKeys.KEY_STREAK, 0)
        )
    }
}
