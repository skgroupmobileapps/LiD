package de.skabs.skgroup.di

import de.skabs.skgroup.data.local.AppDatabase
import de.skabs.skgroup.data.local.QuestionSeeder
import de.skabs.skgroup.data.local.createDatabase
import de.skabs.skgroup.data.repository.BookmarkRepository
import de.skabs.skgroup.data.repository.ExamRepository
import de.skabs.skgroup.data.repository.FeedbackRepository
import de.skabs.skgroup.data.repository.ProgressRepository
import de.skabs.skgroup.data.repository.QuestionRepository
import de.skabs.skgroup.data.repository.SettingsRepository
import de.skabs.skgroup.domain.usecase.BookmarkUseCase
import de.skabs.skgroup.domain.usecase.ExamFlowUseCase
import de.skabs.skgroup.domain.usecase.FeedbackUseCase
import de.skabs.skgroup.domain.usecase.LearningUseCase
import de.skabs.skgroup.domain.usecase.ProgressUseCase
import de.skabs.skgroup.domain.usecase.StatisticsUseCase
import de.skabs.skgroup.feature.exam.ExamViewModel
import de.skabs.skgroup.feature.feedback.FeedbackViewModel
import de.skabs.skgroup.feature.home.HomeViewModel
import de.skabs.skgroup.feature.learn.LearnViewModel
import de.skabs.skgroup.feature.profile.ProfileViewModel
import de.skabs.skgroup.tracking.DefaultTrackingClient
import de.skabs.skgroup.tracking.TrackingClient
import de.skabs.skgroup.tracking.TrackingConsentProvider
import de.skabs.skgroup.widget.WidgetSyncManager
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

