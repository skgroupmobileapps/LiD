import WidgetKit

struct StatsWidgetEntry: TimelineEntry {
    let date: Date
    let correctAnswers: Int
    let accuracyPercent: Float
    let dayStreak: Int
}
