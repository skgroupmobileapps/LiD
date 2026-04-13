package de.skgroup.einburgerungstest.domain.usecase

import de.skgroup.einburgerungstest.core.model.Question
import de.skgroup.einburgerungstest.data.repository.BookmarkRepository

/**
 * Use case for managing question bookmarks.
 */
class BookmarkUseCase(private val bookmarkRepository: BookmarkRepository) {

    fun toggleBookmark(questionId: Int) {
        bookmarkRepository.toggleBookmark(questionId)
    }

    fun isBookmarked(questionId: Int): Boolean {
        return bookmarkRepository.isBookmarked(questionId)
    }

    fun getBookmarkedQuestions(): List<Question> {
        return bookmarkRepository.getBookmarkedQuestions()
    }

    fun getBookmarkCount(): Long {
        return bookmarkRepository.getBookmarkCount()
    }
}
