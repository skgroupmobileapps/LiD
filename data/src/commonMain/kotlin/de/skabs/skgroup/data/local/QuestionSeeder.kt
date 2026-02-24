package de.skabs.skgroup.data.local

import de.skabs.skgroup.core.model.Answer
import de.skabs.skgroup.core.model.FederalState
import de.skabs.skgroup.core.model.Question
import de.skabs.skgroup.core.model.Topic

/**
 * Seeds the database with Einbürgerungstest questions on first launch.
 *
 * Total pool: 300 general questions + 10 per Bundesland (16 × 10 = 160) = 460 questions.
 * Each candidate studies 310: 300 general + 10 for their state.
 */
class QuestionSeeder(private val database: AppDatabase) {

    fun seedIfNeeded() {
        val count = database.appDatabaseQueries.getQuestionCount().executeAsOne()
        if (count > 0) return
        seedQuestions()
    }

    private fun seedQuestions() {
        database.transaction {
            // Seed all questions
            getAllQuestions().forEach { q ->
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
        /**
         * Returns the complete question pool.
         * In production, these would be loaded from a bundled JSON resource file.
         * This contains a representative sample of official Einbürgerungstest questions.
         */
        fun getAllQuestions(): List<Question> {
            return generalQuestions() + allStateQuestions()
        }

        private fun generalQuestions(): List<Question> = listOf(
            // DEMOCRACY_AND_STATE (Questions 1–9)
            Question(1, "Welches Recht gehört zu den Grundrechten in Deutschland?", answers("Waffenbesitz", "Meinungsfreiheit", "Faustrecht", "Recht auf Rache"), 1, Topic.DEMOCRACY_AND_STATE, "Die Meinungsfreiheit ist in Artikel 5 des Grundgesetzes garantiert."),
            Question(2, "Was steht nicht im Grundgesetz von Deutschland?", answers("Die Würde des Menschen ist unantastbar.", "Alle Menschen sind vor dem Gesetz gleich.", "Jeder Bürger muss einer Partei angehören.", "Jeder hat das Recht, seine Meinung frei zu äußern."), 2, Topic.DEMOCRACY_AND_STATE, "Es gibt keine Pflicht zur Parteimitgliedschaft im Grundgesetz."),
            Question(3, "Welches Organ ist das wichtigste Gesetzgebungsorgan in Deutschland?", answers("Der Bundesrat", "Der Bundestag", "Die Bundesregierung", "Das Bundesverfassungsgericht"), 1, Topic.DEMOCRACY_AND_STATE, "Der Bundestag ist das zentrale Gesetzgebungsorgan."),
            Question(4, "Wer wählt den Bundeskanzler / die Bundeskanzlerin in Deutschland?", answers("Das Volk direkt", "Der Bundestag", "Der Bundesrat", "Der Bundespräsident"), 1, Topic.DEMOCRACY_AND_STATE, "Der Bundestag wählt den Bundeskanzler auf Vorschlag des Bundespräsidenten."),
            Question(5, "Wie oft wird der Bundestag in Deutschland gewählt?", answers("Alle 2 Jahre", "Alle 4 Jahre", "Alle 5 Jahre", "Alle 8 Jahre"), 1, Topic.DEMOCRACY_AND_STATE, "Die Legislaturperiode des Bundestages beträgt 4 Jahre."),
            Question(6, "Was ist die Aufgabe des Bundesrates?", answers("Er vertritt die Länder auf Bundesebene.", "Er wählt den Bundeskanzler.", "Er kontrolliert den Bundestag.", "Er bestimmt die Außenpolitik."), 0, Topic.DEMOCRACY_AND_STATE, "Der Bundesrat vertritt die Interessen der Bundesländer auf Bundesebene."),
            Question(7, "Wie heißt die deutsche Verfassung?", answers("Volksgesetz", "Bundesgesetz", "Grundgesetz", "Deutsches Gesetz"), 2, Topic.DEMOCRACY_AND_STATE, "Die Verfassung Deutschlands heißt Grundgesetz."),
            Question(8, "Wann wurde das Grundgesetz der Bundesrepublik Deutschland verabschiedet?", answers("1919", "1933", "1949", "1989"), 2, Topic.DEMOCRACY_AND_STATE, "Das Grundgesetz wurde am 23. Mai 1949 verkündet."),
            Question(9, "Was versteht man unter dem Recht der Freizügigkeit in Deutschland?", answers("Man darf sich eine Wohnung aussuchen.", "Man darf sich seinen Wohnort aussuchen.", "Man darf seinen Arbeitsplatz frei wählen.", "Man darf seine Religion frei wählen."), 1, Topic.DEMOCRACY_AND_STATE, "Freizügigkeit bedeutet, dass jeder Deutsche seinen Wohnort frei wählen kann."),

            // RIGHTS_AND_DUTIES (Questions 10–15)
            Question(10, "Ab welchem Alter darf man in Deutschland wählen?", answers("16 Jahre", "18 Jahre", "21 Jahre", "25 Jahre"), 1, Topic.RIGHTS_AND_DUTIES, "Das aktive Wahlrecht besteht ab 18 Jahren."),
            Question(11, "Was gehört zur Pflicht eines deutschen Staatsbürgers?", answers("Parteimitgliedschaft", "Kirchenmitgliedschaft", "Steuern zahlen", "Eine Waffe besitzen"), 2, Topic.RIGHTS_AND_DUTIES, "Steuern zahlen ist eine Bürgerpflicht in Deutschland."),
            Question(12, "Was ist in Deutschland verboten?", answers("Demonstrationen", "Streiks", "Kinderarbeit", "Kirchenaustritt"), 2, Topic.RIGHTS_AND_DUTIES, "Kinderarbeit ist in Deutschland verboten."),
            Question(13, "In Deutschland dürfen Menschen offen etwas gegen die Regierung sagen, weil...", answers("hier Religionsfreiheit gilt.", "die Menschen Steuern zahlen.", "die Menschen das Wahlrecht haben.", "hier Meinungsfreiheit gilt."), 3, Topic.RIGHTS_AND_DUTIES, "Die Meinungsfreiheit erlaubt es, die Regierung offen zu kritisieren."),
            Question(14, "Was bedeutet Gleichberechtigung?", answers("Alle Menschen haben die gleiche Ausbildung.", "Alle Menschen haben die gleichen Rechte.", "Alle Menschen haben die gleiche Sprache.", "Alle Menschen sind gleich alt."), 1, Topic.RIGHTS_AND_DUTIES, "Gleichberechtigung bedeutet gleiche Rechte für alle Menschen."),
            Question(15, "Was bedeutet die Religionsfreiheit?", answers("Man muss einer Religion angehören.", "Man darf keine Religion haben.", "Jeder Mensch kann seine Religion frei wählen.", "Der Staat bestimmt die Religion."), 2, Topic.RIGHTS_AND_DUTIES, "Religionsfreiheit bedeutet, dass jeder seine Religion frei wählen oder auch keine haben kann."),

            // HISTORY (Questions 16–21)
            Question(16, "Wann war der Zweite Weltkrieg?", answers("1914 – 1918", "1933 – 1945", "1939 – 1945", "1949 – 1989"), 2, Topic.HISTORY, "Der Zweite Weltkrieg dauerte von 1939 bis 1945."),
            Question(17, "Was geschah am 9. November 1989 in Deutschland?", answers("Die Deutsche Einheit", "Die Wiedervereinigung", "Der Fall der Berliner Mauer", "Die Gründung der BRD"), 2, Topic.HISTORY, "Am 9. November 1989 fiel die Berliner Mauer."),
            Question(18, "Wann wurde Deutschland wiedervereinigt?", answers("1945", "1949", "1961", "1990"), 3, Topic.HISTORY, "Die Wiedervereinigung Deutschlands fand am 3. Oktober 1990 statt."),
            Question(19, "Was war der Holocaust?", answers("Ein Krieg", "Der Völkermord an den europäischen Juden", "Eine Revolution", "Ein Friedensvertrag"), 1, Topic.HISTORY, "Der Holocaust war der systematische Völkermord an den europäischen Juden durch die Nationalsozialisten."),
            Question(20, "Wer regierte Deutschland während des Nationalsozialismus?", answers("Wilhelm II.", "Adolf Hitler", "Konrad Adenauer", "Helmut Kohl"), 1, Topic.HISTORY, "Adolf Hitler regierte Deutschland als Diktator von 1933 bis 1945."),
            Question(21, "Was war die Berliner Mauer?", answers("Eine Grenze zwischen Ost- und Westberlin", "Eine Grenze zwischen Deutschland und Polen", "Ein Baudenkmal", "Eine Autobahn"), 0, Topic.HISTORY, "Die Berliner Mauer teilte Berlin von 1961 bis 1989 in Ost und West."),

            // SOCIETY_AND_CULTURE (Questions 22–25)
            Question(22, "Welche Schulpflicht gilt in Deutschland?", answers("6 Jahre", "8 Jahre", "9 Jahre", "12 Jahre"), 2, Topic.SOCIETY_AND_CULTURE, "In den meisten Bundesländern gilt eine Schulpflicht von mindestens 9 Jahren."),
            Question(23, "Welcher Tag ist in Deutschland ein gesetzlicher Feiertag?", answers("1. August", "1. Mai", "1. Juni", "1. Juli"), 1, Topic.SOCIETY_AND_CULTURE, "Der 1. Mai (Tag der Arbeit) ist ein gesetzlicher Feiertag."),
            Question(24, "Was ist in Deutschland ein Brauch an Weihnachten?", answers("Einen Weihnachtsbaum aufstellen", "Ostereier suchen", "Kürbisse schnitzen", "Fasching feiern"), 0, Topic.SOCIETY_AND_CULTURE, "An Weihnachten stellt man traditionell einen Weihnachtsbaum auf."),
            Question(25, "Welche Versicherung ist in Deutschland Pflicht?", answers("Lebensversicherung", "Krankenversicherung", "Reiseversicherung", "Hausratversicherung"), 1, Topic.SOCIETY_AND_CULTURE, "Die Krankenversicherung ist in Deutschland gesetzlich vorgeschrieben."),

            // SYMBOLS_AND_GEOGRAPHY (Questions 26–30)
            Question(26, "Welche Farben hat die deutsche Flagge?", answers("Rot-Weiß-Blau", "Schwarz-Rot-Gold", "Schwarz-Weiß-Rot", "Blau-Weiß-Rot"), 1, Topic.SYMBOLS_AND_GEOGRAPHY, "Die deutsche Flagge ist Schwarz-Rot-Gold."),
            Question(27, "Was ist die Hauptstadt von Deutschland?", answers("München", "Hamburg", "Berlin", "Frankfurt"), 2, Topic.SYMBOLS_AND_GEOGRAPHY, "Berlin ist seit 1990 die Hauptstadt des wiedervereinigten Deutschlands."),
            Question(28, "Wie viele Bundesländer hat Deutschland?", answers("10", "14", "16", "18"), 2, Topic.SYMBOLS_AND_GEOGRAPHY, "Deutschland besteht aus 16 Bundesländern."),
            Question(29, "Wie heißt die Nationalhymne der Bundesrepublik Deutschland?", answers("Freude schöner Götterfunken", "Das Lied der Deutschen", "Heil dir im Siegerkranz", "Auferstanden aus Ruinen"), 1, Topic.SYMBOLS_AND_GEOGRAPHY, "Die Nationalhymne ist die dritte Strophe des Liedes der Deutschen."),
            Question(30, "Welcher Fluss ist der längste in Deutschland?", answers("Die Elbe", "Die Donau", "Der Rhein", "Die Weser"), 2, Topic.SYMBOLS_AND_GEOGRAPHY, "Der Rhein ist mit ca. 865 km in Deutschland der längste Fluss.")
        )

        private fun allStateQuestions(): List<Question> {
            var id = 301
            return FederalState.entries.flatMap { state ->
                stateQuestionsFor(state, id).also { id += it.size }
            }
        }

        private fun stateQuestionsFor(state: FederalState, startId: Int): List<Question> {
            val stateName = state.displayName
            return listOf(
                Question(startId, "Wie heißt die Hauptstadt von $stateName?", answers("Berlin", getCapital(state), "München", "Hamburg"), 1, Topic.FEDERAL_STATE, "${getCapital(state)} ist die Landeshauptstadt von $stateName.", state),
                Question(startId + 1, "Welches Wappen gehört zu $stateName?", answers("Löwe", "Adler", getStateSymbol(state), "Bär"), 2, Topic.FEDERAL_STATE, "Das Wappen von $stateName zeigt ${getStateSymbol(state)}.", state)
            )
        }

        private fun getCapital(state: FederalState): String = when (state) {
            FederalState.BADEN_WUERTTEMBERG -> "Stuttgart"
            FederalState.BAVARIA -> "München"
            FederalState.BERLIN -> "Berlin"
            FederalState.BRANDENBURG -> "Potsdam"
            FederalState.BREMEN -> "Bremen"
            FederalState.HAMBURG -> "Hamburg"
            FederalState.HESSE -> "Wiesbaden"
            FederalState.MECKLENBURG_VORPOMMERN -> "Schwerin"
            FederalState.LOWER_SAXONY -> "Hannover"
            FederalState.NORTH_RHINE_WESTPHALIA -> "Düsseldorf"
            FederalState.RHINELAND_PALATINATE -> "Mainz"
            FederalState.SAARLAND -> "Saarbrücken"
            FederalState.SAXONY -> "Dresden"
            FederalState.SAXONY_ANHALT -> "Magdeburg"
            FederalState.SCHLESWIG_HOLSTEIN -> "Kiel"
            FederalState.THURINGIA -> "Erfurt"
        }

        private fun getStateSymbol(state: FederalState): String = when (state) {
            FederalState.BADEN_WUERTTEMBERG -> "drei Löwen"
            FederalState.BAVARIA -> "weiß-blaue Rauten"
            FederalState.BERLIN -> "einen Bären"
            FederalState.BRANDENBURG -> "einen roten Adler"
            FederalState.BREMEN -> "einen Schlüssel"
            FederalState.HAMBURG -> "eine Burg"
            FederalState.HESSE -> "einen Löwen"
            FederalState.MECKLENBURG_VORPOMMERN -> "einen Stier"
            FederalState.LOWER_SAXONY -> "ein Sachsenross"
            FederalState.NORTH_RHINE_WESTPHALIA -> "Rhein und Westfalenross"
            FederalState.RHINELAND_PALATINATE -> "ein Kreuz"
            FederalState.SAARLAND -> "einen Löwen und ein Kreuz"
            FederalState.SAXONY -> "einen Löwen"
            FederalState.SAXONY_ANHALT -> "einen Adler"
            FederalState.SCHLESWIG_HOLSTEIN -> "ein Nesselblatt"
            FederalState.THURINGIA -> "einen Löwen"
        }

        private fun answers(a: String, b: String, c: String, d: String): List<Answer> = listOf(
            Answer("A", a), Answer("B", b), Answer("C", c), Answer("D", d)
        )
    }
}
