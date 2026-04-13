package de.skgroup.einburgerungstest.analytics

import org.koin.core.module.Module

/**
 * Koin module for analytics provider dependency injection.
 *
 * Provides platform-specific Firebase-backed sink implementation.
 */
expect fun analyticsModule(): Module
