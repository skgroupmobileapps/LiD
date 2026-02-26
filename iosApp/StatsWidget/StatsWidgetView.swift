import SwiftUI
import WidgetKit

struct StatsWidgetView: View {
    var entry: StatsWidgetEntry
    
    // Design system colors
    private let primaryGreen = Color(red: 0.106, green: 0.369, blue: 0.231) // #1B5E3B
    private let primaryGreenSurface = Color(red: 0.910, green: 0.961, blue: 0.914) // #E8F5E9
    private let backgroundCream = Color(red: 1.0, green: 0.973, blue: 0.941) // #FFF8F0
    private let textPrimary = Color(red: 0.102, green: 0.102, blue: 0.102) // #1A1A1A
    private let textSecondary = Color(red: 0.420, green: 0.420, blue: 0.420) // #6B6B6B
    
    var body: some View {
        VStack(spacing: 12) {
            // Stats Row
            HStack(spacing: 0) {
                StatItemView(
                    icon: "checkmark.circle.fill",
                    iconColor: primaryGreen,
                    value: "\(entry.correctAnswers)",
                    label: "Richtig"
                )
                
                Spacer()
                
                StatItemView(
                    icon: "target",
                    iconColor: primaryGreen,
                    value: "\(Int(entry.accuracyPercent))%",
                    label: "Genauigkeit"
                )
                
                Spacer()
                
                StatItemView(
                    icon: "flame.fill",
                    iconColor: .orange,
                    value: "\(entry.dayStreak)",
                    label: "Streak"
                )
            }
            .padding(.horizontal, 8)
            
            // Action Buttons Row
            HStack(spacing: 8) {
                Link(destination: URL(string: "kmpexam://learn")!) {
                    Text("Lernen")
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 36)
                        .background(primaryGreen)
                        .cornerRadius(8)
                }
                
                Link(destination: URL(string: "kmpexam://exam")!) {
                    Text("Prüfung")
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(primaryGreen)
                        .frame(maxWidth: .infinity)
                        .frame(height: 36)
                        .background(primaryGreenSurface)
                        .cornerRadius(8)
                }
            }
        }
        .padding(16)
        .widgetURL(URL(string: "kmpexam://home"))
    }
}

struct StatItemView: View {
    let icon: String
    let iconColor: Color
    let value: String
    let label: String
    
    private let textPrimary = Color(red: 0.102, green: 0.102, blue: 0.102)
    private let textSecondary = Color(red: 0.420, green: 0.420, blue: 0.420)
    
    var body: some View {
        VStack(spacing: 4) {
            Image(systemName: icon)
                .font(.system(size: 20))
                .foregroundColor(iconColor)
            
            Text(value)
                .font(.system(size: 24, weight: .bold))
                .foregroundColor(textPrimary)
            
            Text(label)
                .font(.system(size: 12))
                .foregroundColor(textSecondary)
        }
        .frame(maxWidth: .infinity)
    }
}

#Preview(as: .systemMedium) {
    StatsWidget()
} timeline: {
    StatsWidgetEntry(date: .now, correctAnswers: 42, accuracyPercent: 85.5, dayStreak: 7)
}
