package de.skgroup.einburgerungstest.data.widget

import de.skgroup.einburgerungstest.core.model.WidgetStats
import platform.Foundation.NSUserDefaults

/**
 * iOS implementation using UserDefaults with App Group.
 */
actual class WidgetDataExporter {
    private val userDefaults: NSUserDefaults? by lazy {
        NSUserDefaults(suiteName = WidgetDataKeys.IOS_APP_GROUP)
    }
    
    actual fun exportStats(stats: WidgetStats) {
        userDefaults?.apply {
            setInteger(stats.correctAnswers.toLong(), WidgetDataKeys.KEY_CORRECT)
            setFloat(stats.accuracyPercent, WidgetDataKeys.KEY_ACCURACY)
            setInteger(stats.dayStreak.toLong(), WidgetDataKeys.KEY_STREAK)
            synchronize()
        }
    }
    
    actual fun readStats(): WidgetStats {
        return userDefaults?.let { defaults ->
            WidgetStats(
                correctAnswers = defaults.integerForKey(WidgetDataKeys.KEY_CORRECT).toInt(),
                accuracyPercent = defaults.floatForKey(WidgetDataKeys.KEY_ACCURACY),
                dayStreak = defaults.integerForKey(WidgetDataKeys.KEY_STREAK).toInt()
            )
        } ?: WidgetStats()
    }
}
