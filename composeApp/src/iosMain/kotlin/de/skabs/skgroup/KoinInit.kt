package de.skabs.skgroup

import de.skabs.skgroup.data.local.DatabaseDriverFactory
import de.skabs.skgroup.di.appModule
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
            module {
                single { DatabaseDriverFactory() }
            }
        )
    }
}
