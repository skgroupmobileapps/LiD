package de.skgroup.einburgerungstest.data.local

import de.skgroup.einburgerungstest.core.model.FederalState
import de.skgroup.einburgerungstest.core.model.Topic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Tests for [QuestionSeeder.parseQuestions].
 *
 * These are pure-Kotlin (no SQLite) tests that verify JSON deserialization, domain
 * model mapping and edge-case handling for the Einbürgerungstest catalogue.
 */
class QuestionSeederParserTest {

    // ── helpers ──────────────────────────────────────────────────────────────────

    private fun parse(json: String) = QuestionSeeder.parseQuestions(json)

    // ── tests ─────────────────────────────────────────────────────────────────────

    @Test
    fun parseQuestions_returnsAllQuestionsFromJson() {
        val questions = parse(SAMPLE_JSON)
        assertEquals(3, questions.size)
    }

    @Test
    fun parseQuestions_assignsAnswerLabelsInOrder() {
        val q = parse(SAMPLE_JSON).first()
        assertEquals("A", q.answers[0].label)
        assertEquals("B", q.answers[1].label)
        assertEquals("C", q.answers[2].label)
        assertEquals("D", q.answers[3].label)
    }

    @Test
    fun parseQuestions_assignsAnswerText() {
        val q = parse(SAMPLE_JSON).first()
        assertEquals("Antwort A", q.answers[0].text)
        assertEquals("Antwort B", q.answers[1].text)
        assertEquals("Antwort C", q.answers[2].text)
        assertEquals("Antwort D", q.answers[3].text)
    }

    @Test
    fun parseQuestions_mapsCorrectAnswerIndex() {
        val questions = parse(SAMPLE_JSON)
        assertEquals(3, questions[0].correctAnswerIndex)
        assertEquals(0, questions[1].correctAnswerIndex)
    }

    @Test
    fun parseQuestions_usesMinus1WhenCorrectAnswerIndexAbsent() {
        val questions = parse(SAMPLE_NO_CORRECT_ANSWER_JSON)
        assertEquals(-1, questions.first().correctAnswerIndex)
    }

    @Test
    fun parseQuestions_mapsGeneralQuestionWithoutFederalState() {
        val generalQ = parse(SAMPLE_JSON).first { it.federalState == null }
        assertNull(generalQ.federalState)
    }

    @Test
    fun parseQuestions_mapsStateQuestionWithFederalState() {
        val stateQ = parse(SAMPLE_JSON).first { it.federalState != null }
        assertEquals(FederalState.BADEN_WUERTTEMBERG, stateQ.federalState)
    }

    @Test
    fun parseQuestions_mapsImageName() {
        val stateQ = parse(SAMPLE_JSON).first { it.imageName != null }
        assertEquals("q_301.png", stateQ.imageName)
    }

    @Test
    fun parseQuestions_nullImageNameWhenAbsent() {
        val q = parse(SAMPLE_JSON).first { it.imageName == null }
        assertNull(q.imageName)
    }

    @Test
    fun parseQuestions_mapsKnownTopic() {
        val q = parse(SAMPLE_JSON).first { it.id == 1 }
        assertEquals(Topic.DEMOCRACY_AND_STATE, q.topic)
    }

    @Test
    fun parseQuestions_fallsBackToSocietyAndCultureForUnknownTopic() {
        val questions = parse(SAMPLE_UNKNOWN_TOPIC_JSON)
        assertEquals(Topic.SOCIETY_AND_CULTURE, questions.first().topic)
    }

    @Test
    fun parseQuestions_mapsExplanation() {
        val q = parse(SAMPLE_JSON).first()
        assertEquals("Eine Erklärung.", q.explanation)
    }

    @Test
    fun parseQuestions_emptyExplanationWhenAbsent() {
        val q = parse(SAMPLE_JSON).first { it.id == 301 }
        assertEquals("", q.explanation)
    }

    @Test
    fun parseQuestions_preservesQuestionId() {
        val questions = parse(SAMPLE_JSON)
        assertEquals(1, questions[0].id)
        assertEquals(2, questions[1].id)
        assertEquals(301, questions[2].id)
    }

    @Test
    fun parseQuestions_handlesEmptyQuestionList() {
        val questions = parse(EMPTY_CATALOGUE_JSON)
        assertEquals(0, questions.size)
    }

    @Test
    fun parseQuestions_ignoresUnknownJsonFields() {
        // Should not throw even if extra keys appear in the JSON.
        val questions = parse(SAMPLE_EXTRA_FIELDS_JSON)
        assertEquals(1, questions.size)
    }

    // ── fixtures ──────────────────────────────────────────────────────────────────

    companion object {
        private val SAMPLE_JSON = """
            {
              "catalogDate": "07.05.2025",
              "totalGeneral": 2,
              "totalState": 1,
              "questions": [
                {
                  "id": 1,
                  "text": "Frage 1",
                  "answers": ["Antwort A", "Antwort B", "Antwort C", "Antwort D"],
                  "correctAnswerIndex": 3,
                  "topic": "DEMOCRACY_AND_STATE",
                  "explanation": "Eine Erklärung."
                },
                {
                  "id": 2,
                  "text": "Frage 2",
                  "answers": ["Antwort A", "Antwort B", "Antwort C", "Antwort D"],
                  "correctAnswerIndex": 0,
                  "topic": "SOCIETY_AND_CULTURE",
                  "explanation": "Zweite Erklärung."
                },
                {
                  "id": 301,
                  "text": "Frage zum Bundesland",
                  "answers": ["Bild 1", "Bild 2", "Bild 3", "Bild 4"],
                  "correctAnswerIndex": 0,
                  "topic": "FEDERAL_STATE",
                  "explanation": "",
                  "federalState": "BADEN_WUERTTEMBERG",
                  "imageName": "q_301.png"
                }
              ]
            }
        """.trimIndent()

        private val SAMPLE_NO_CORRECT_ANSWER_JSON = """
            {
              "catalogDate": "07.05.2025",
              "totalGeneral": 1,
              "totalState": 0,
              "questions": [
                {
                  "id": 1,
                  "text": "Frage ohne Antwort",
                  "answers": ["A", "B", "C", "D"],
                  "topic": "DEMOCRACY_AND_STATE",
                  "explanation": ""
                }
              ]
            }
        """.trimIndent()

        private val SAMPLE_UNKNOWN_TOPIC_JSON = """
            {
              "catalogDate": "07.05.2025",
              "totalGeneral": 1,
              "totalState": 0,
              "questions": [
                {
                  "id": 99,
                  "text": "Frage unbekanntes Thema",
                  "answers": ["A", "B", "C", "D"],
                  "correctAnswerIndex": 0,
                  "topic": "UNKNOWN_TOPIC_XYZ",
                  "explanation": ""
                }
              ]
            }
        """.trimIndent()

        private val EMPTY_CATALOGUE_JSON = """
            {
              "catalogDate": "07.05.2025",
              "totalGeneral": 0,
              "totalState": 0,
              "questions": []
            }
        """.trimIndent()

        private val SAMPLE_EXTRA_FIELDS_JSON = """
            {
              "catalogDate": "07.05.2025",
              "totalGeneral": 1,
              "totalState": 0,
              "someUnknownField": "ignored",
              "questions": [
                {
                  "id": 1,
                  "text": "Frage",
                  "answers": ["A", "B", "C", "D"],
                  "correctAnswerIndex": 1,
                  "topic": "DEMOCRACY_AND_STATE",
                  "explanation": "",
                  "anotherUnknownField": 42
                }
              ]
            }
        """.trimIndent()
    }
}
