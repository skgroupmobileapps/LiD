package de.skabs.skgroup.tracking

import org.koin.core.module.Module
import org.koin.dsl.module

fun trackingModule(): Module = module {
    single<TrackingConsentProvider> { DisabledByDefaultConsentProvider() }
    single<TrackingClient> { DefaultTrackingClient(get(), getAll()) }
}
