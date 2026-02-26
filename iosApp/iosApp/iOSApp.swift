import SwiftUI

@main
struct iOSApp: App {
    @State private var initialDeeplinkRoute: String? = nil
    
    var body: some Scene {
        WindowGroup {
            ContentView(initialDeeplinkRoute: initialDeeplinkRoute)
                .onOpenURL { url in
                    // Handle deeplink
                    if let route = parseDeeplink(url) {
                        initialDeeplinkRoute = route
                    }
                }
        }
    }
    
    private func parseDeeplink(_ url: URL) -> String? {
        guard url.scheme == "kmpexam" else { return nil }
        
        switch url.host {
        case "home": return "home"
        case "learn": return "learn"
        case "exam": return "exam_intro"
        default: return nil
        }
    }
}