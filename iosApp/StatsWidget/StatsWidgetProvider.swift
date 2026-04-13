import WidgetKit
import Foundation

struct StatsWidgetProvider: TimelineProvider {
    
    // App Group identifier - must match the one in Kotlin code
    private let appGroupId = "group.de.skgroup.einburgerungstest.kmpexam"
    
    // Keys matching Kotlin WidgetDataKeys
    private let keyCorrect = "widget_correct"
    private let keyAccuracy = "widget_accuracy"
    private let keyStreak = "widget_streak"
    
    func placeholder(in context: Context) -> StatsWidgetEntry {
        StatsWidgetEntry(date: Date(), correctAnswers: 0, accuracyPercent: 0, dayStreak: 0)
    }
    
    func getSnapshot(in context: Context, completion: @escaping (StatsWidgetEntry) -> Void) {
        let entry = readStatsEntry()
        completion(entry)
    }
    
    func getTimeline(in context: Context, completion: @escaping (Timeline<StatsWidgetEntry>) -> Void) {
        let entry = readStatsEntry()
        
        // Refresh every 15 minutes
        let nextUpdate = Calendar.current.date(byAdding: .minute, value: 15, to: Date())!
        let timeline = Timeline(entries: [entry], policy: .after(nextUpdate))
        
        completion(timeline)
    }
    
    private func readStatsEntry() -> StatsWidgetEntry {
        guard let userDefaults = UserDefaults(suiteName: appGroupId) else {
            return StatsWidgetEntry(date: Date(), correctAnswers: 0, accuracyPercent: 0, dayStreak: 0)
        }
        
        let correct = userDefaults.integer(forKey: keyCorrect)
        let accuracy = userDefaults.float(forKey: keyAccuracy)
        let streak = userDefaults.integer(forKey: keyStreak)
        
        return StatsWidgetEntry(
            date: Date(),
            correctAnswers: correct,
            accuracyPercent: accuracy,
            dayStreak: streak
        )
    }
}
