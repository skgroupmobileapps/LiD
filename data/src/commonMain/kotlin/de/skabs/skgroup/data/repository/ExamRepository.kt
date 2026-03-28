package de.skabs.skgroup.data.repository

import de.skabs.skgroup.core.model.ExamHistoryEntry
import de.skabs.skgroup.core.model.ExamResult
import de.skabs.skgroup.core.model.FederalState
import de.skabs.skgroup.data.local.AppDatabase

/**
 * Repository for exam history operations.
 */
class ExamRepository(private val database: AppDatabase) {

    fun saveExamResult(result: ExamResult) {
        database.appDatabaseQueries.insertExamHistory(
            totalQuestions = result.totalQuestions.toLong(),
            correctCount = result.correctCount.toLong(),
            wrongCount = result.wrongCount.toLong(),
            passed = if (result.passed) 1L else 0L,
            scorePercent = result.scorePercent.toDouble(),
            timeSpentMs = result.timeSpentMs,
            federalState = result.federalState.name,
            timestampMs = result.timestampMs
        )
    }

    fun getExamHistory(): List<ExamHistoryEntry> {
        return database.appDatabaseQueries.getExamHistory().executeAsList().map { entity ->
            ExamHistoryEntry(
                id = entity.id,
                totalQuestions = entity.totalQuestions.toInt(),
                correctCount = entity.correctCount.toInt(),
                passed = entity.passed == 1L,
                scorePercent = entity.scorePercent.toFloat(),
                timestampMs = entity.timestampMs,
                federalState = FederalState.valueOf(entity.federalState)
            )
        }
    }

    fun getExamCount(): Long {
        return database.appDatabaseQueries.getExamCount().executeAsOne()
    }

    fun getPassedExamCount(): Long {
        return database.appDatabaseQueries.getPassedExamCount().executeAsOne()
    }

    fun getAverageScore(): Double? {
        return database.appDatabaseQueries.getAverageScore().executeAsOneOrNull()?.AVG
    }

    fun clearExamHistory() {
        database.appDatabaseQueries.clearExamHistory()
    }
}
