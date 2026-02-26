package de.skabs.skgroup

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { App() }

fun MainViewControllerWithDeeplink(deeplinkRoute: String?) = ComposeUIViewController { 
    App(initialDeeplinkRoute = deeplinkRoute) 
}