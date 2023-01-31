package wordsvirtuoso

import java.io.File
import java.io.IOException

fun main(args: Array<String>) {
    val app = WordsVirtuoso()

    app.start(args)
}


class WordsVirtuoso {
    fun start(args: Array<String>) {

        when (val loadingResult = loadWordsLists(args)) {
            is LoadingFailed -> {
                println(loadingResult.msg)
                return
            }

            is LoadingSuccessful -> {
                println("Words Virtuoso")
                playGame(loadingResult.allWords, loadingResult.candidates)
            }
        }

    }

    private fun playGame(allWords: List<String>, candidates: List<String>) {
        val random = java.util.Random()
        val secretWord = candidates[random.nextInt(candidates.size)]
        val clues = mutableListOf<String>()
        val incorrectChars = mutableListOf<Char>()
        var tries = 1

        val startTime = System.currentTimeMillis()

        while (playRound(secretWord, allWords, clues, incorrectChars)) {
            tries += 1
        }

        val endTime = System.currentTimeMillis()
        val durationInSeconds = (endTime - startTime) / 1000

        if (tries == 1) {
            println("Amazing luck! The solution was found at once.")
        } else {
            println("The solution was found after $tries tries in $durationInSeconds seconds.")
        }

    }

    private fun playRound(
        secretWord: String,
        allWords: List<String>,
        clues: MutableList<String>,
        incorrectChars: MutableList<Char>
    ): Boolean {
        val guess = promptWith("Input a 5-letter word:")

        if (guess == "exit") {
            println("The game is over.")
            return false
        }

        val checked = checkString(guess)
        if (checked is CheckFailed) {
            println(checked.failMessage)
            return true
        }

        if (!allWords.map { it.lowercase() }.contains(guess.lowercase().trim())) {
            println("The input word isn't included in my words list.")
            return true
        }


        val (clueString, incorrectLetters) = clueString(guess, secretWord)
        clues.add(clueString)
        clues.forEach(::println)

        if (guess == secretWord) {
            println("Correct!")
            return false
        }

        incorrectChars.addAll(incorrectLetters)
        val incorrectCharsUppercase = incorrectChars.distinct()
            .sorted()
            .joinToString("")
            .uppercase()
        println(Color.AZURE.coloring(incorrectCharsUppercase))

        return true
    }

    private fun clueString(guess: String, secret: String): Pair<String, List<Char>> {
        val uppercaseSecret = secret.uppercase()
        val incorrectCharsEntered = mutableListOf<Char>()

        return guess.withIndex().joinToString(separator = "") { (idx, ch) ->
            val uppercase = ch.uppercase()
            if (uppercase == secret[idx].uppercase()) {
                Color.GREEN.coloring(uppercase)
            } else if (uppercaseSecret.contains(uppercase)) {
                Color.YELLOW.coloring(uppercase)
            } else {
                incorrectCharsEntered.add(ch)
                Color.GREY.coloring(uppercase)
            }
        } to incorrectCharsEntered.toList()
    }

    private enum class Color(val coloring: (String) -> String) {
        GREEN({ str -> "\u001B[48:5:10m$str\u001B[0m" }),
        YELLOW({ str -> "\u001B[48:5:11m$str\u001B[0m" }),
        GREY({ str -> "\u001B[48:5:7m$str\u001B[0m" }),
        AZURE({ str -> "\u001B[48:5:14m$str\u001B[0m" })
    }

    private sealed class LoadingResult
    private class LoadingFailed(val msg: String) : LoadingResult()
    private class LoadingSuccessful(val candidates: List<String>, val allWords: List<String>) : LoadingResult()

    private fun loadWordsLists(args: Array<String>): LoadingResult {
        if (args.size != 2) {
            return LoadingFailed("Error: Wrong number of arguments.")
        }

        val allWords = FileHandler.linesFromFileOrNull(args[0])
        if (allWords == null) {
            return LoadingFailed("Error: The words file ${args[0]} doesn't exist.")
        }

        val candidates = FileHandler.linesFromFileOrNull(args[1])
        if (candidates == null) {
            return LoadingFailed("Error: The candidate words file ${args[1]} doesn't exist.")
        }

        val invalidFromAllWords = allWords.count { checkString(it) is CheckFailed }
        if (invalidFromAllWords > 0) {
            return LoadingFailed("Error: $invalidFromAllWords invalid words were found in the ${args[0]} file.")
        }

        val invalidFromCandidates = candidates.count { checkString(it) is CheckFailed }
        if (invalidFromCandidates > 0) {
            return LoadingFailed("Error: $invalidFromCandidates invalid words were found in the ${args[1]} file.")
        }

        val candidatesNotIncluded = candidates
            .map { it.lowercase() }
            .minus(allWords.map { it.lowercase() }.toSet())
            .size

        if (candidatesNotIncluded > 0) {
            return LoadingFailed("Error: $candidatesNotIncluded candidate words are not included in the ${args[0]} file.")
        }

        return LoadingSuccessful(candidates, allWords)
    }

    private fun promptWith(prompt: String): String {
        println(prompt)
        return readln()
    }

    private fun checkString(string: String): CheckingResult {
        val desiredLength = 5

        if (string.length != desiredLength) {
            return CheckFailed("The input isn't a $desiredLength-letter word.")
        }

        if (string.any { ch -> ch !in 'a'..'z' && ch !in 'A'..'Z' }) {
            return CheckFailed("One or more letters of the input aren't valid.")
        }

        if (string.toSet().size != string.length) {
            return CheckFailed("The input has duplicate letters.")
        }

        return CheckPassed(string)
    }

    private sealed class CheckingResult
    private data class CheckPassed(val validString: String) : CheckingResult()
    private data class CheckFailed(val failMessage: String) : CheckingResult()
}

object FileHandler {
    fun linesFromFileOrNull(filename: String): List<String>? {
        return try {
            File(filename).readLines()
        } catch (ioExc: IOException) {
            null
        }
    }
}