package de.skabs.skgroup

import android.app.Application
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
                appModule(questionsJsonProvider = {
                    // Read bundled questions JSON from compose resources
                    // The file is in the resources module which has compose resources configured
                    try {
                        context.assets.open("composeResources/kmpexam.resources.generated.resources/files/questions_de.json")
                            .bufferedReader()
                            .use { it.readText() }
                    } catch (_: Exception) {
                        // Fallback: empty catalogue (seeder will produce 0 questions)
                        """{"catalogDate":"","totalGeneral":0,"totalState":0,"questions":[]}"""
                    }
                }),
                module {
                    single { DatabaseDriverFactory(context) }
                }
            )
        }
    }
}
