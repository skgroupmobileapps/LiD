package de.skgroup.einburgerungstest.analytics

import de.skgroup.einburgerungstest.tracking.TrackingSink
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS analytics module providing Firebase implementation.
 */
actual fun analyticsModule(): Module = module {
    single<TrackingSink> { FirebaseAnalyticsSink() }
}
