package de.skabs.skgroup

import android.app.Application
import de.skabs.skgroup.data.local.DatabaseDriverFactory
import de.skabs.skgroup.di.appModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

class EinbuergerungApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                appModule,
                module {
                    single { DatabaseDriverFactory(this@EinbuergerungApp) }
                }
            )
        }
    }
}
