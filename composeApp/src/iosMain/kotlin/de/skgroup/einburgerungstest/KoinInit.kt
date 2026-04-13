package de.skgroup.einburgerungstest

import de.skgroup.einburgerungstest.analytics.analyticsModule
import de.skgroup.einburgerungstest.data.local.DatabaseDriverFactory
import de.skgroup.einburgerungstest.di.appModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Initialize Koin for iOS.
 * Called from Swift's AppDelegate/App struct.
 */
fun initKoinIos() {
    startKoin {
        modules(
            appModule(),
            analyticsModule(),
            module {
                single { DatabaseDriverFactory() }
            }
        )
    }
}
