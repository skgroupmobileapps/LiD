package de.skgroup.einburgerungstest.core.util

import de.skgroup.einburgerungstest.core.model.ExamResult

/**
 * Scoring logic for the Einbürgerungstest.
 *
 * Rules:
 * - 33 questions total (30 general + 3 state-specific)
 * - Pass threshold: 17 correct answers (51%)
 */
object Scoring {

    /**
     * Determine if the candidate passed the exam.
     */
    fun isPassed(correctCount: Int, totalQuestions: Int = ExamResult.TOTAL_EXAM_QUESTIONS): Boolean {
        return correctCount >= ExamResult.PASS_THRESHOLD
    }

    /**
     * Calculate accuracy as a percentage (0–100).
     */
    fun calculateAccuracy(correct: Int, total: Int): Float {
        if (total <= 0) return 0f
        return (correct.toFloat() / total.toFloat()) * 100f
    }

    /**
     * Calculate the score percentage for an exam.
     */
    fun calculateScorePercent(correctCount: Int, totalQuestions: Int): Float {
        return calculateAccuracy(correctCount, totalQuestions)
    }

    /**
     * Calculate how many more correct answers are needed to pass.
     * Returns 0 if already passing.
     */
    fun questionsNeededToPass(currentCorrect: Int): Int {
        val needed = ExamResult.PASS_THRESHOLD - currentCorrect
        return if (needed > 0) needed else 0
    }
}
