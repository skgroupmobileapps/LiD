package de.skabs.skgroup.di

import de.skabs.skgroup.data.local.AppDatabase
import de.skabs.skgroup.data.local.QuestionSeeder
import de.skabs.skgroup.data.local.createDatabase
import de.skabs.skgroup.data.repository.BookmarkRepository
import de.skabs.skgroup.data.repository.ExamRepository
import de.skabs.skgroup.data.repository.ProgressRepository
import de.skabs.skgroup.data.repository.QuestionRepository
import de.skabs.skgroup.data.repository.SettingsRepository
import de.skabs.skgroup.domain.usecase.BookmarkUseCase
import de.skabs.skgroup.domain.usecase.ExamFlowUseCase
import de.skabs.skgroup.domain.usecase.LearningUseCase
import de.skabs.skgroup.domain.usecase.ProgressUseCase
import de.skabs.skgroup.domain.usecase.StatisticsUseCase
import de.skabs.skgroup.feature.exam.ExamViewModel
import de.skabs.skgroup.feature.home.HomeViewModel
import de.skabs.skgroup.feature.learn.LearnViewModel
import de.skabs.skgroup.feature.profile.ProfileViewModel
import de.skabs.skgroup.widget.WidgetSyncManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin dependency injection module for the entire application.
 *
 * Provides:
 * - Database & Seeder
 * - Repositories
 * - Use Cases
 * - ViewModels
 *
 * @param questionsJsonProvider Lambda that returns the JSON string of the question catalogue.
 *   This is provided by the app layer which has access to Compose Resources.
 */
fun appModule(questionsJsonProvider: () -> String) = module {
    // Database
    single<AppDatabase> { createDatabase(get()) }
    single { QuestionSeeder(get(), questionsJsonProvider) }

    // Repositories
    singleOf(::QuestionRepository)
    singleOf(::ExamRepository)
    singleOf(::ProgressRepository)
    singleOf(::BookmarkRepository)
    singleOf(::SettingsRepository)

    // Widget
    single { WidgetSyncManager() }

    // Use Cases
    singleOf(::ExamFlowUseCase)
    singleOf(::LearningUseCase)
    singleOf(::BookmarkUseCase)
    singleOf(::StatisticsUseCase)
    singleOf(::ProgressUseCase)

    // ViewModels - use single() for manual lifecycle management with iOS compatibility
    single { HomeViewModel(get(), get(), get()) }
    single { LearnViewModel(get(), get(), get(), get()) }
    single { ExamViewModel(get(), get()) }
    single { ProfileViewModel(get(), get(), get(), get()) }
}

