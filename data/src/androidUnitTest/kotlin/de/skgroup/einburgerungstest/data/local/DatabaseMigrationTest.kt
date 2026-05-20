package de.skgroup.einburgerungstest.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Migration tests for the [AppDatabase] schema upgrade from version 1 → 2.
 *
 * Strategy under test:
 *  - [AppDatabase.Schema.migrate] (which executes `1.sqm`) clears [QuestionEntity].
 *  - All other tables (UserAnswerEntity, BookmarkEntity, ExamHistoryEntity,
 *    SettingsEntity) are **not** touched by the migration.
 *  - After the migration, [QuestionSeeder.seedIfNeeded] detects `count == 0`
 *    and re-seeds the new catalogue, restoring all foreign-key references.
 *
 * Tests run against an in-memory SQLite database via [JdbcSqliteDriver] so no
 * Android device or emulator is required.
 */
class DatabaseMigrationTest {

    private lateinit var driver: SqlDriver
    private lateinit var db: AppDatabase

    @BeforeTest
    fun setUp() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        db = AppDatabase(driver)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    // ── migration effect on QuestionEntity ────────────────────────────────────────

    @Test
    fun migration_clearsAllQuestions() {
        insertSampleQuestion(id = 1)
        insertSampleQuestion(id = 2)
        assertEquals(2, questionCount())

        applyMigration()

        assertEquals(0, questionCount())
    }

    @Test
    fun migration_withNoQuestions_succeeds() {
        assertEquals(0, questionCount())
        applyMigration()
        assertEquals(0, questionCount())
    }

    // ── migration preserves non-question tables ───────────────────────────────────

    @Test
    fun migration_preservesBookmarks() {
        insertSampleQuestion(id = 10)
        db.appDatabaseQueries.insertBookmark(questionId = 10L, timestampMs = 1_000L)
        assertEquals(1, db.appDatabaseQueries.getBookmarkCount().executeAsOne())

        applyMigration()

        // Bookmark survives even though the question row was deleted.
        // SQLite does not enforce FK constraints by default; the reference is
        // restored when the seeder re-inserts questions with the same IDs.
        assertEquals(1, db.appDatabaseQueries.getBookmarkCount().executeAsOne())
    }

    @Test
    fun migration_preservesUserAnswers() {
        insertSampleQuestion(id = 10)
        db.appDatabaseQueries.insertAnswer(
            questionId = 10L,
            selectedIndex = 2L,
            isCorrect = 1L,
            timestampMs = 2_000L,
            mode = "learn"
        )
        assertEquals(1, db.appDatabaseQueries.getTotalAnswerCount().executeAsOne())

        applyMigration()

        assertEquals(1, db.appDatabaseQueries.getTotalAnswerCount().executeAsOne())
    }

    @Test
    fun migration_preservesExamHistory() {
        db.appDatabaseQueries.insertExamHistory(
            totalQuestions = 33L,
            correctCount = 28L,
            wrongCount = 5L,
            passed = 1L,
            scorePercent = 84.8,
            timeSpentMs = 300_000L,
            federalState = "KEINE",
            timestampMs = 3_000L
        )
        assertEquals(1, db.appDatabaseQueries.getExamCount().executeAsOne())

        applyMigration()

        assertEquals(1, db.appDatabaseQueries.getExamCount().executeAsOne())
    }

    @Test
    fun migration_preservesSettings() {
        db.appDatabaseQueries.insertSetting(key = "federal_state", value_ = "BAYERN")
        db.appDatabaseQueries.insertSetting(key = "analytics_enabled", value_ = "true")

        applyMigration()

        assertEquals("BAYERN", db.appDatabaseQueries.getSetting("federal_state").executeAsOneOrNull())
        assertEquals("true", db.appDatabaseQueries.getSetting("analytics_enabled").executeAsOneOrNull())
    }

    @Test
    fun migration_preservesMultipleNonQuestionTablesSimultaneously() {
        insertSampleQuestion(id = 5)
        db.appDatabaseQueries.insertBookmark(questionId = 5L, timestampMs = 1L)
        db.appDatabaseQueries.insertAnswer(5L, 0L, 1L, 2L, "exam")
        db.appDatabaseQueries.insertExamHistory(33L, 28L, 5L, 1L, 84.8, 300_000L, "KEINE", 3L)
        db.appDatabaseQueries.insertSetting("key1", "value1")

        applyMigration()

        assertEquals(0, questionCount())
        assertEquals(1, db.appDatabaseQueries.getBookmarkCount().executeAsOne())
        assertEquals(1, db.appDatabaseQueries.getTotalAnswerCount().executeAsOne())
        assertEquals(1, db.appDatabaseQueries.getExamCount().executeAsOne())
        assertEquals("value1", db.appDatabaseQueries.getSetting("key1").executeAsOneOrNull())
    }

    // ── re-seeding after migration ────────────────────────────────────────────────

    @Test
    fun seedIfNeeded_reseedsAfterMigrationClearsQuestions() {
        insertSampleQuestion(id = 1)
        assertEquals(1, questionCount())

        applyMigration()
        assertEquals(0, questionCount())

        // seedIfNeeded detects count == 0 and re-seeds
        val seeder = QuestionSeeder(db) { SAMPLE_SEED_JSON }
        seeder.seedIfNeeded()

        assertEquals(3, questionCount())
    }

    @Test
    fun seedIfNeeded_doesNotReseedWhenQuestionsAlreadyPresent() {
        val seeder = QuestionSeeder(db) { SAMPLE_SEED_JSON }
        seeder.seedIfNeeded()
        val firstCount = questionCount()

        // Calling again must be a no-op
        seeder.seedIfNeeded()

        assertEquals(firstCount, questionCount())
    }

    @Test
    fun reSeed_replacesExistingQuestionsWithNewContent() {
        val seeder = QuestionSeeder(db) { SAMPLE_SEED_JSON }
        seeder.seedIfNeeded()
        assertEquals(3, questionCount())

        // Force re-seed with a different catalogue (one question)
        val updatedSeeder = QuestionSeeder(db) { SINGLE_QUESTION_JSON }
        updatedSeeder.reSeed()

        assertEquals(1, questionCount())
    }

    @Test
    fun reSeed_preservesBookmarksWhenQuestionIdsAreStable() {
        val seeder = QuestionSeeder(db) { SAMPLE_SEED_JSON }
        seeder.seedIfNeeded()
        db.appDatabaseQueries.insertBookmark(questionId = 1L, timestampMs = 999L)
        assertEquals(1, db.appDatabaseQueries.getBookmarkCount().executeAsOne())

        // Re-seed with updated content but same IDs
        seeder.reSeed()

        // Bookmark still present; question 1 exists again
        assertEquals(1, db.appDatabaseQueries.getBookmarkCount().executeAsOne())
        assertTrue(questionCount() > 0)
    }

    @Test
    fun migration_thenSeedIfNeeded_restoresForeignKeyReferences() {
        insertSampleQuestion(id = 1)
        db.appDatabaseQueries.insertBookmark(questionId = 1L, timestampMs = 1L)

        applyMigration()

        // Bookmark survives with dangling FK
        assertEquals(1, db.appDatabaseQueries.getBookmarkCount().executeAsOne())
        assertEquals(0, questionCount())

        // After re-seeding, question 1 exists again → FK reference is valid
        val seeder = QuestionSeeder(db) { SAMPLE_SEED_JSON }
        seeder.seedIfNeeded()

        val bookmarkedQuestions = db.appDatabaseQueries.getBookmarkedQuestions().executeAsList()
        assertEquals(1, bookmarkedQuestions.size)
        assertEquals(1L, bookmarkedQuestions.first().id)
    }

    // ── helpers ───────────────────────────────────────────────────────────────────

    /** Applies the v1 → v2 migration (executes 1.sqm). */
    private fun applyMigration() {
        AppDatabase.Schema.migrate(driver, oldVersion = 1L, newVersion = 2L)
    }

    private fun questionCount() = db.appDatabaseQueries.getQuestionCount().executeAsOne()

    private fun insertSampleQuestion(id: Long) {
        db.appDatabaseQueries.insertQuestion(
            id = id,
            text = "Frage $id",
            answerA = "A", answerB = "B", answerC = "C", answerD = "D",
            correctIndex = 0L,
            topic = "DEMOCRACY_AND_STATE",
            explanation = "",
            federalState = null,
            imageName = null
        )
    }

    // ── fixtures ──────────────────────────────────────────────────────────────────

    companion object {
        /** 3-question catalogue used for seeder integration tests. */
        val SAMPLE_SEED_JSON = """
            {
              "catalogDate": "07.05.2025",
              "totalGeneral": 2,
              "totalState": 1,
              "questions": [
                {
                  "id": 1,
                  "text": "Frage 1",
                  "answers": ["A1", "B1", "C1", "D1"],
                  "correctAnswerIndex": 3,
                  "topic": "DEMOCRACY_AND_STATE",
                  "explanation": "Erklärung 1."
                },
                {
                  "id": 2,
                  "text": "Frage 2",
                  "answers": ["A2", "B2", "C2", "D2"],
                  "correctAnswerIndex": 0,
                  "topic": "SOCIETY_AND_CULTURE",
                  "explanation": "Erklärung 2."
                },
                {
                  "id": 301,
                  "text": "Länderfrage",
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

        val SINGLE_QUESTION_JSON = """
            {
              "catalogDate": "07.05.2025",
              "totalGeneral": 1,
              "totalState": 0,
              "questions": [
                {
                  "id": 1,
                  "text": "Aktualisierte Frage 1",
                  "answers": ["A", "B", "C", "D"],
                  "correctAnswerIndex": 1,
                  "topic": "DEMOCRACY_AND_STATE",
                  "explanation": "Neue Erklärung."
                }
              ]
            }
        """.trimIndent()
    }
}
