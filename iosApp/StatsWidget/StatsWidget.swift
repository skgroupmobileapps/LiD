import WidgetKit
import SwiftUI

struct StatsWidget: Widget {
    let kind: String = "StatsWidget"
    
    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: StatsWidgetProvider()) { entry in
            StatsWidgetView(entry: entry)
                .containerBackground(Color("BackgroundCream"), for: .widget)
        }
        .configurationDisplayName("Lernstatistik")
        .description("Zeigt Ihren Lernfortschritt")
        .supportedFamilies([.systemMedium])
    }
}

#Preview(as: .systemMedium) {
    StatsWidget()
} timeline: {
    StatsWidgetEntry(date: .now, correctAnswers: 42, accuracyPercent: 85.5, dayStreak: 7)
}
