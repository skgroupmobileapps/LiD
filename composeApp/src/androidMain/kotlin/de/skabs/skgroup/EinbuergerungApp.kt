package de.skabs.skgroup

import android.app.Application
import de.skabs.skgroup.analytics.analyticsModule
import de.skabs.skgroup.core.ContextProvider
import de.skabs.skgroup.data.local.DatabaseDriverFactory
import de.skabs.skgroup.di.appModule
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
