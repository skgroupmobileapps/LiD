package de.skabs.skgroup.analytics

import de.skabs.skgroup.tracking.TrackingSink
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android analytics module providing Firebase implementation.
 */
actual fun analyticsModule(): Module = module {
    single<TrackingSink> { FirebaseAnalyticsSink() }
}
