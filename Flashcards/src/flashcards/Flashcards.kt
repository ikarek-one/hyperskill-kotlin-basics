package flashcards

import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException

class Flashcards(
    private val importFilename: String? = null,
    private val exportFilename: String? = null,
    private val runner: IRunner = LoggedConsoleRunner()
) {

    private val flashcards: MutableList<Flashcard> = mutableListOf()


    fun takeActions() {
        if (importFilename != null) {
            importFromFile(importFilename)
        }

        while (true) {
            runner.say("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")

            when (val action = runner.ask()) {
                "add" -> addSingleCard()
                "remove" -> removeSingleCard()
                "import" -> importFromFile()
                "export" -> exportToFile()
                "ask" -> askDefinition()
                "log" -> saveLog()
                "hardest card" -> printHardest()
                "reset stats" -> resetStats()

                "exit" -> {
                    runner.say("Bye bye!")
                    if (exportFilename != null) {
                        exportToFile(exportFilename)
                    }
                    return
                }

                else -> throw RuntimeException("Unknown action: '$action'!")
            }
        }
    }

    private fun addSingleCard() {
        runner.say("The card:")
        val term = runner.ask()
        if (hasTerm(term)) {
            runner.say("The card \"$term\" already exists.")
            return
        }

        runner.say("The definition of the card:")
        val definition = runner.ask()
        if (hasDefinition(definition)) {
            runner.say("The definition \"$definition\" already exists.")
            return
        }

        flashcards.add(Flashcard(term, definition))
        runner.say("The pair (\"$term\":\"$definition\") has been added.")
    }

    private fun removeSingleCard() {
        runner.say("Which card?")
        val term = runner.ask()
        if (hasTerm(term)) {
            flashcards.removeIf { it.term == term }
            runner.say("The card has been removed")
        } else {
            runner.say("Can't remove \"$term\": there is no such card.")
        }
    }

    private fun askDefinition() {
        runner.say("How many times to ask?")
        val times = runner.ask().toInt()

        for (i in 0 until times) {
            val card = flashcards[i % flashcards.size]

            runner.say("Print the definition of \"${card.term}\":")
            val userDef = runner.ask()
            val isCorrect = card.isAnswerCorrect(userDef)

            val rightTerm: String? =
                flashcards.asSequence().filterNot { it == card }.find { c -> c.isAnswerCorrect(userDef) }?.term

            val response = if (isCorrect) {
                "Correct!"
            } else if (rightTerm != null) {
                "Wrong. The right answer is \"${card.definition}\", but your definition is correct for \"$rightTerm\"."
            } else {
                "Wrong. The right answer is \"${card.definition}\"."
            }

            if (!isCorrect) {
                card.incrementMistakes()
            }

            runner.say(response)
        }

    }

    private fun exportToFile() {
        runner.say("File name:")
        val filename = runner.ask()

        exportToFile(filename)
    }

    private fun exportToFile(filename: String) {
        val gson = Gson()
        val jsonList = gson.toJson(flashcards)

        val file = File(filename)
        file.writeText(jsonList)

        runner.say("${flashcards.size} cards have been saved.")
    }

    private fun importFromFile() {
        runner.say("File name:")
        val filename = runner.ask()

        importFromFile(filename)
    }

    private fun importFromFile(filename: String) {
        val file = File(filename)

        try {
            val jsonText: String = file.readText()

            val gson = Gson()
            val classMarker = emptyArray<Flashcard>().javaClass
            val jsonArray = gson.fromJson(jsonText, classMarker)

            for (card in jsonArray) {
                if (hasTerm(card.term)) {
                    flashcards.removeIf { it.term == card.term }
                }
                flashcards.add(card)
            }

            runner.say("${jsonArray.size} cards have been loaded.")
        } catch (exc: FileNotFoundException) {
            runner.say("File not found.")
        }
    }

    private fun printHardest() {
        val maxMistakes = flashcards.maxOfOrNull { it.getMistakes() } ?: 0

        if (maxMistakes == 0) {
            runner.say("There are no cards with errors.")
            return
        }

        val hardest = flashcards.filter { it.getMistakes() == maxMistakes }
        val msg = if (hardest.size == 1) {
            "The hardest card is \"${hardest[0].term}\". You have $maxMistakes errors answering it."
        } else {
            "The hardest cards are " +
                    hardest.joinToString { c -> "\"${c.term}\"" } +
                    ". You have $maxMistakes errors answering them."
        }
        runner.say(msg)
    }

    private fun saveLog() {
        runner.say("File name:")
        val filename = runner.ask()
        val file = File(filename)
        file.writeText(runner.log() ?: "")
        runner.say("The log has been saved.")
    }

    private fun resetStats() {
        for (card in flashcards) {
            card.resetMistakes()
        }

        runner.say("Card statistics have been reset.")
    }

    private fun hasTerm(term: String): Boolean {
        return flashcards.any { it.term == term }
    }

    private fun hasDefinition(definition: String): Boolean {
        return flashcards.any { it.definition == definition }
    }

}
