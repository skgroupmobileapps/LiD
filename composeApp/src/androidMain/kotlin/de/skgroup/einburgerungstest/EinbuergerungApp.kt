package de.skgroup.einburgerungstest

import android.app.Application
import de.skgroup.einburgerungstest.analytics.analyticsModule
import de.skgroup.einburgerungstest.core.ContextProvider
import de.skgroup.einburgerungstest.data.local.DatabaseDriverFactory
import de.skgroup.einburgerungstest.di.appModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

class EinbuergerungApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val context = this@EinbuergerungApp
        
        // Initialize ContextProvider for platform utilities
        ContextProvider.init(context)
        startKoin {
            modules(
                appModule(),
                analyticsModule(),
                module {
                    single { DatabaseDriverFactory(context) }
                }
            )
        }
    }
}
