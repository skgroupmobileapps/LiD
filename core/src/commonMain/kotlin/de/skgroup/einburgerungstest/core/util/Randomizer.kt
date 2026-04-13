package de.skgroup.einburgerungstest.core.util

import de.skgroup.einburgerungstest.core.model.ExamResult
import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Question
import de.skgroup.einburgerungstest.core.model.Topic
import kotlin.random.Random

/**
 * Randomizer for selecting exam questions.
 *
 * A real Einbürgerungstest exam has:
 * - 30 random questions from the 300 general pool
 * - 3 random questions from the 10 state-specific pool for the candidate's Bundesland
 * = 33 total questions
 */
object Randomizer {

    /**
     * Select 33 questions for an exam: 30 general + 3 from the state pool.
     *
     * @param allQuestions Complete pool of questions (460 total)
     * @param federalState The candidate's Bundesland
     * @param random Random instance for deterministic testing
     * @return List of 33 selected questions in randomized order
     */
    fun selectExamQuestions(
        allQuestions: List<Question>,
        federalState: FederalState,
        random: Random = Random
    ): List<Question> {
        val generalQuestions = allQuestions.filter { it.federalState == null }
        val stateQuestions = allQuestions.filter { it.federalState == federalState }

        val selectedGeneral = generalQuestions
            .shuffled(random)
            .take(ExamResult.GENERAL_QUESTIONS_COUNT)

        val selectedState = stateQuestions
            .shuffled(random)
            .take(ExamResult.STATE_QUESTIONS_COUNT)

        return (selectedGeneral + selectedState).shuffled(random)
    }

    /**
     * Get all questions available for a specific candidate (300 general + 10 state).
     */
    fun getCandidateQuestions(
        allQuestions: List<Question>,
        federalState: FederalState
    ): List<Question> {
        return allQuestions.filter {
            it.federalState == null || it.federalState == federalState
        }
    }

    /**
     * Get questions filtered by a specific topic.
     */
    fun getQuestionsByTopic(
        allQuestions: List<Question>,
        topic: Topic,
        federalState: FederalState? = null
    ): List<Question> {
        return allQuestions.filter { question ->
            question.topic == topic &&
                (federalState == null || question.federalState == null || question.federalState == federalState)
        }
    }
}
