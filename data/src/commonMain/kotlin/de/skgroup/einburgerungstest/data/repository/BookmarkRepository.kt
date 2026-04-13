package de.skgroup.einburgerungstest.data.repository

import de.skgroup.einburgerungstest.core.model.Bookmark
import de.skgroup.einburgerungstest.core.model.Question
import de.skgroup.einburgerungstest.core.model.Answer
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Topic
import de.skgroup.einburgerungstest.data.local.AppDatabase
import kotlinx.datetime.Clock

/**
 * Repository for bookmark operations.
 */
class BookmarkRepository(private val database: AppDatabase) {

    fun toggleBookmark(questionId: Int) {
        val isCurrentlyBookmarked = isBookmarked(questionId)
        if (isCurrentlyBookmarked) {
            database.appDatabaseQueries.deleteBookmark(questionId.toLong())
        } else {
            val now = Clock.System.now().toEpochMilliseconds()
            database.appDatabaseQueries.insertBookmark(questionId.toLong(), now)
        }
    }

    fun isBookmarked(questionId: Int): Boolean {
        val count = database.appDatabaseQueries.isBookmarked(questionId.toLong()).executeAsOne()
        return count > 0
    }

    fun getBookmarkCount(): Long {
        return database.appDatabaseQueries.getBookmarkCount().executeAsOne()
    }

    fun getBookmarkedQuestions(): List<Question> {
        return database.appDatabaseQueries.getBookmarkedQuestions().executeAsList().map { entity ->
            Question(
                id = entity.id.toInt(),
                text = entity.text,
                answers = listOf(
                    Answer("A", entity.answerA),
                    Answer("B", entity.answerB),
                    Answer("C", entity.answerC),
                    Answer("D", entity.answerD)
                ),
                correctAnswerIndex = entity.correctIndex.toInt(),
                topic = Topic.valueOf(entity.topic),
                explanation = entity.explanation,
                federalState = entity.federalState?.let { FederalState.valueOf(it) }
            )
        }
    }
}
