package de.skabs.skgroup.analytics

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS analytics module providing Firebase implementation.
 */
actual fun analyticsModule(): Module = module {
    single<AnalyticsTracker> { FirebaseAnalyticsTracker() }
}
