import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    var initialDeeplinkRoute: String?
    
    func makeUIViewController(context: Context) -> UIViewController {
        if let route = initialDeeplinkRoute {
            return MainViewControllerKt.MainViewControllerWithDeeplink(deeplinkRoute: route)
        }
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var initialDeeplinkRoute: String?
    
    var body: some View {
        ComposeView(initialDeeplinkRoute: initialDeeplinkRoute)
            .ignoresSafeArea()
    }
}



