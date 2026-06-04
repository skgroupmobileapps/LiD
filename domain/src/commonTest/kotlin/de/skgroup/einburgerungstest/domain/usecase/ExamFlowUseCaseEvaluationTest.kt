package de.skgroup.einburgerungstest.domain.usecase

import de.skgroup.einburgerungstest.core.model.Answer
import de.skgroup.einburgerungstest.core.model.ExamSession
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Question
import de.skgroup.einburgerungstest.core.model.Topic
import kotlin.test.Test
import kotlin.test.assertEquals

class ExamFlowUseCaseEvaluationTest {

    @Test
    fun evaluateExamAnswers_countsUnansweredAsReviewItems() {
        val question1 = testQuestion(id = 1, correctAnswerIndex = 1)
        val question2 = testQuestion(id = 2, correctAnswerIndex = 2)
        val question3 = testQuestion(id = 3, correctAnswerIndex = 3)
        val session = ExamSession(
            questions = listOf(question1, question2, question3),
            answers = mutableMapOf(
                1 to 1,
                2 to 0
            ),
            startTimeMs = 0L,
            federalState = FederalState.BERLIN
        )

        val evaluation = evaluateExamAnswers(session)

        assertEquals(1, evaluation.correctCount)
        assertEquals(2, evaluation.wrongAnswers.size)
        assertEquals(2, evaluation.wrongAnswers[0].question.id)
        assertEquals(0, evaluation.wrongAnswers[0].selectedAnswerIndex)
        assertEquals(3, evaluation.wrongAnswers[1].question.id)
        assertEquals(-1, evaluation.wrongAnswers[1].selectedAnswerIndex)
    }

    private fun testQuestion(id: Int, correctAnswerIndex: Int): Question {
        return Question(
            id = id,
            text = "Question $id",
            answers = listOf(
                Answer("A", "A$id"),
                Answer("B", "B$id"),
                Answer("C", "C$id"),
                Answer("D", "D$id")
            ),
            correctAnswerIndex = correctAnswerIndex,
            topic = Topic.DEMOCRACY_AND_STATE,
            explanation = "Explanation $id"
        )
    }
}
