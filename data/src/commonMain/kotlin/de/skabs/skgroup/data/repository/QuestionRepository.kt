package de.skabs.skgroup.data.repository

import de.skabs.skgroup.core.model.Answer
import de.skabs.skgroup.core.model.FederalState
import de.skabs.skgroup.core.model.Question
import de.skabs.skgroup.core.model.Topic
import de.skabs.skgroup.data.local.AppDatabase

/**
 * Repository for accessing the question pool.
 */
class QuestionRepository(private val database: AppDatabase) {

    fun getAllQuestions(): List<Question> {
        return database.appDatabaseQueries.getAllQuestions().executeAsList().map { it.toQuestion() }
    }

    fun getQuestionById(id: Int): Question? {
        return database.appDatabaseQueries.getQuestionById(id.toLong()).executeAsOneOrNull()?.toQuestion()
    }

    fun getQuestionsByTopic(topic: Topic): List<Question> {
        return database.appDatabaseQueries.getQuestionsByTopic(topic.name).executeAsList().map { it.toQuestion() }
    }

    fun getGeneralQuestions(): List<Question> {
        return database.appDatabaseQueries.getGeneralQuestions().executeAsList().map { it.toQuestion() }
    }

    fun getCandidateQuestions(federalState: FederalState): List<Question> {
        return database.appDatabaseQueries.getCandidateQuestions(federalState.name).executeAsList().map { it.toQuestion() }
    }

    fun getQuestionsByFederalState(federalState: FederalState): List<Question> {
        return database.appDatabaseQueries.getQuestionsByFederalState(federalState.name).executeAsList().map { it.toQuestion() }
    }

    fun getQuestionCount(): Long {
        return database.appDatabaseQueries.getQuestionCount().executeAsOne()
    }

    fun insertQuestion(question: Question) {
        database.appDatabaseQueries.insertQuestion(
            id = question.id.toLong(),
            text = question.text,
            answerA = question.answers[0].text,
            answerB = question.answers[1].text,
            answerC = question.answers[2].text,
            answerD = question.answers[3].text,
            correctIndex = question.correctAnswerIndex.toLong(),
            topic = question.topic.name,
            explanation = question.explanation,
            federalState = question.federalState?.name
        )
    }
}

/**
 * Map a SQLDelight entity to the domain Question model.
 */
private fun de.skabs.skgroup.data.local.QuestionEntity.toQuestion(): Question {
    return Question(
        id = id.toInt(),
        text = text,
        answers = listOf(
            Answer("A", answerA),
            Answer("B", answerB),
            Answer("C", answerC),
            Answer("D", answerD)
        ),
        correctAnswerIndex = correctIndex.toInt(),
        topic = Topic.valueOf(topic),
        explanation = explanation,
        federalState = federalState?.let { FederalState.valueOf(it) }
    )
}
