package de.skgroup.einburgerungstest.di

import de.skgroup.einburgerungstest.data.local.AppDatabase
import de.skgroup.einburgerungstest.data.local.QuestionSeeder
import de.skgroup.einburgerungstest.data.local.createDatabase
import de.skgroup.einburgerungstest.data.repository.BookmarkRepository
import de.skgroup.einburgerungstest.data.repository.ExamRepository
import de.skgroup.einburgerungstest.data.repository.FeedbackRepository
import de.skgroup.einburgerungstest.data.repository.ProgressRepository
import de.skgroup.einburgerungstest.data.repository.QuestionRepository
import de.skgroup.einburgerungstest.data.repository.SettingsRepository
import de.skgroup.einburgerungstest.domain.usecase.BookmarkUseCase
import de.skgroup.einburgerungstest.domain.usecase.ExamFlowUseCase
import de.skgroup.einburgerungstest.domain.usecase.FeedbackUseCase
import de.skgroup.einburgerungstest.domain.usecase.LearningUseCase
import de.skgroup.einburgerungstest.domain.usecase.ProgressUseCase
import de.skgroup.einburgerungstest.domain.usecase.StatisticsUseCase
import de.skgroup.einburgerungstest.feature.exam.ExamViewModel
import de.skgroup.einburgerungstest.feature.feedback.FeedbackViewModel
import de.skgroup.einburgerungstest.feature.home.HomeViewModel
import de.skgroup.einburgerungstest.feature.learn.LearnViewModel
import de.skgroup.einburgerungstest.feature.profile.ProfileViewModel
import de.skgroup.einburgerungstest.tracking.DefaultTrackingClient
import de.skgroup.einburgerungstest.tracking.TrackingClient
import de.skgroup.einburgerungstest.tracking.TrackingConsentProvider
import de.skgroup.einburgerungstest.widget.WidgetSyncManager
import kmpexam.resources.generated.resources.Res
import kotlinx.coroutines.runBlocking
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
 * Questions JSON is loaded from Compose Resources (cross-platform).
 */
fun appModule() = module {
    // Database
    single<AppDatabase> { createDatabase(get()) }
    single {
        val questionsJson = runBlocking {
            Res.readBytes("files/questions_de.json").decodeToString()
        }
        QuestionSeeder(get()) { questionsJson }
    }

    // Repositories
    singleOf(::QuestionRepository)
    singleOf(::ExamRepository)
    singleOf(::ProgressRepository)
    singleOf(::BookmarkRepository)
    singleOf(::SettingsRepository)
    single { FeedbackRepository() }

    // Tracking
    single<TrackingConsentProvider> { SettingsTrackingConsentProvider(get()) }
    single<TrackingClient> { DefaultTrackingClient(get(), getAll()) }

    // Widget
    single { WidgetSyncManager() }

    // Use Cases
    singleOf(::ExamFlowUseCase)
    singleOf(::LearningUseCase)
    singleOf(::BookmarkUseCase)
    singleOf(::StatisticsUseCase)
    singleOf(::ProgressUseCase)
    singleOf(::FeedbackUseCase)

    // ViewModels - use factory() so that each screen gets a fresh instance that Compose can clear.
    factory { HomeViewModel(get(), get(), get(), get()) }
    factory { LearnViewModel(get(), get(), get(), get(), get(), get()) }
    factory { ExamViewModel(get(), get(), get()) }
    factory { ProfileViewModel(get(), get(), get(), get(), get()) }
    factory { FeedbackViewModel(get(), get(), get(), get()) }
}

