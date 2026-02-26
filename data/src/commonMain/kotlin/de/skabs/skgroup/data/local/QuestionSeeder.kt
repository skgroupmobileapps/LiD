package de.skabs.skgroup.data.local

import de.skabs.skgroup.core.model.Answer
import de.skabs.skgroup.core.model.FederalState
import de.skabs.skgroup.core.model.Question
import de.skabs.skgroup.core.model.Topic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * JSON data models for question catalogue deserialization.
 */
@Serializable
private data class QuestionCatalogue(
    val catalogDate: String = "",
    val totalGeneral: Int = 0,
    val totalState: Int = 0,
    val questions: List<JsonQuestion> = emptyList()
)

@Serializable
private data class JsonQuestion(
    val id: Int,
    val text: String,
    val answers: List<String>,
    val correctAnswerIndex: Int? = null,
    val topic: String,
    val explanation: String = "",
    val federalState: String? = null
)

/**
 * Seeds the database with Einbürgerungstest questions on first launch.
 *
 * Total pool: 300 general questions + 10 per Bundesland (16 × 10 = 160) = 460 questions.
 * Each candidate studies 310: 300 general + 10 for their state.
 *
 * Questions are loaded from a JSON string provided via [questionsJsonProvider].
 * The JSON is parsed once and cached.
 */
class QuestionSeeder(
    private val database: AppDatabase,
    private val questionsJsonProvider: () -> String
) {

    fun seedIfNeeded() {
        val count = database.appDatabaseQueries.getQuestionCount().executeAsOne()
        if (count > 0) return
        seedQuestions()
    }

    private fun seedQuestions() {
        val questions = parseQuestions(questionsJsonProvider())
        database.transaction {
            questions.forEach { q ->
                database.appDatabaseQueries.insertQuestion(
                    id = q.id.toLong(),
                    text = q.text,
                    answerA = q.answers[0].text,
                    answerB = q.answers[1].text,
                    answerC = q.answers[2].text,
                    answerD = q.answers[3].text,
                    correctIndex = q.correctAnswerIndex.toLong(),
                    topic = q.topic.name,
                    explanation = q.explanation,
                    federalState = q.federalState?.name
                )
            }
        }
    }

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        /**
         * Parse questions from JSON string into domain [Question] objects.
         */
        fun parseQuestions(jsonText: String): List<Question> {
            val catalogue = json.decodeFromString<QuestionCatalogue>(jsonText)
            return catalogue.questions.map { jq ->
                val topic = try {
                    Topic.valueOf(jq.topic)
                } catch (_: Exception) {
                    Topic.SOCIETY_AND_CULTURE
                }
                val federalState = jq.federalState?.let { fs ->
                    try {
                        FederalState.valueOf(fs)
                    } catch (_: Exception) {
                        null
                    }
                }
                Question(
                    id = jq.id,
                    text = jq.text,
                    answers = jq.answers.mapIndexed { index, text ->
                        Answer(label = ('A' + index).toString(), text = text)
                    },
                    // Use -1 for questions without correct answers yet (to be updated later)
                    correctAnswerIndex = jq.correctAnswerIndex ?: -1,
                    topic = topic,
                    explanation = jq.explanation,
                    federalState = federalState
                )
            }
        }
    }
}
