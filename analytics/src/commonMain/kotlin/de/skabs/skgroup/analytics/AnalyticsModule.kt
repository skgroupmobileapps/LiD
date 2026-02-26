package de.skabs.skgroup.analytics

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module for analytics dependency injection.
 *
 * Provides platform-specific [AnalyticsTracker] implementation.
 */
expect fun analyticsModule(): Module

/**
 * Common analytics module with shared bindings.
 * Platform modules extend this with their specific implementations.
 */
internal val commonAnalyticsModule = module {
    // Platform-specific modules will provide the AnalyticsTracker implementation
}
