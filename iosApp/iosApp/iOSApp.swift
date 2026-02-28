import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    @State private var initialDeeplinkRoute: String? = nil
    
    init() {
        // Initialize Koin DI before any Kotlin composables are invoked
        KoinInitKt.doInitKoinIos()
    }
    
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