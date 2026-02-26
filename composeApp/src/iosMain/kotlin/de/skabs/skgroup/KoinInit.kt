package de.skabs.skgroup

import de.skabs.skgroup.data.local.DatabaseDriverFactory
import de.skabs.skgroup.di.appModule
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

/**
 * Initialize Koin for iOS.
 * Called from Swift's AppDelegate/App struct.
 */
@OptIn(ExperimentalForeignApi::class)
fun initKoinIos() {
    startKoin {
        modules(
            appModule(questionsJsonProvider = {
                // Read bundled questions JSON from Compose Resources in iOS bundle
                try {
                    val path = NSBundle.mainBundle.pathForResource(
                        "compose-resources/kmpexam.composeapp.generated.resources/files/questions_de",
                        "json"
                    )
                    path?.let {
                        NSString.stringWithContentsOfFile(it, NSUTF8StringEncoding, null) as? String
                    } ?: """{"catalogDate":"","totalGeneral":0,"totalState":0,"questions":[]}"""
                } catch (_: Exception) {
                    """{"catalogDate":"","totalGeneral":0,"totalState":0,"questions":[]}"""
                }
            }),
            module {
                single { DatabaseDriverFactory() }
            }
        )
    }
}
