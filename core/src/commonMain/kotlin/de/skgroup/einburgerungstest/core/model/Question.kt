package de.skgroup.einburgerungstest.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a topic category in the Einbürgerungstest.
 */
@Serializable
enum class Topic(val displayName: String, val description: String, val icon: String) {
    DEMOCRACY_AND_STATE(
        displayName = "Democracy & State",
        description = "German political system, constitution, and democratic principles",
        icon = "🏛"
    ),
    RIGHTS_AND_DUTIES(
        displayName = "Rights & Duties",
        description = "Fundamental rights, civic duties, and legal principles",
        icon = "⚖"
    ),
    HISTORY(
        displayName = "History",
        description = "German history from unification to reunification",
        icon = "📖"
    ),
    SOCIETY_AND_CULTURE(
        displayName = "Society & Culture",
        description = "Social systems, education, religion, and daily life",
        icon = "👥"
    ),
    SYMBOLS_AND_GEOGRAPHY(
        displayName = "Symbols & Geography",
        description = "National symbols, geography, and federal states",
        icon = "📍"
    ),
    FEDERAL_STATE(
        displayName = "Federal State",
        description = "Questions specific to your Bundesland",
        icon = "🗺"
    );
}

/**
 * The 16 German federal states (Bundesländer).
 */
@Serializable
enum class FederalState(val displayName: String) {
    BADEN_WUERTTEMBERG("Baden-Württemberg"),
    BAVARIA("Bayern"),
    BERLIN("Berlin"),
    BRANDENBURG("Brandenburg"),
    BREMEN("Bremen"),
    HAMBURG("Hamburg"),
    HESSE("Hessen"),
    MECKLENBURG_VORPOMMERN("Mecklenburg-Vorpommern"),
    LOWER_SAXONY("Niedersachsen"),
    NORTH_RHINE_WESTPHALIA("Nordrhein-Westfalen"),
    RHINELAND_PALATINATE("Rheinland-Pfalz"),
    SAARLAND("Saarland"),
    SAXONY("Sachsen"),
    SAXONY_ANHALT("Sachsen-Anhalt"),
    SCHLESWIG_HOLSTEIN("Schleswig-Holstein"),
    THURINGIA("Thüringen");
}

/**
 * A single answer option for a question.
 */
@Serializable
data class Answer(
    val label: String,  // "A", "B", "C", "D"
    val text: String
)

/**
 * A question in the Einbürgerungstest.
 *
 * @param id Unique question identifier (1–460)
 * @param text The question text
 * @param answers List of 4 answer options
 * @param correctAnswerIndex Index of the correct answer (0–3)
 * @param topic The topic category this question belongs to
 * @param explanation Explanation shown after answering (learn mode only)
 * @param federalState If non-null, this question is specific to this Bundesland
 */
@Serializable
data class Question(
    val id: Int,
    val text: String,
    val answers: List<Answer>,
    val correctAnswerIndex: Int,
    val topic: Topic,
    val explanation: String,
    val federalState: FederalState? = null,
    val imageName: String? = null
)
