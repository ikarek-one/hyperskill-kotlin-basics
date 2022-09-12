package flashcards


data class Flashcard(val term: String, val definition: String) {
    private var mistakes = 0

    fun isAnswerCorrect(userAnswer: String): Boolean {
        return definition == userAnswer
    }

    fun getMistakes(): Int = mistakes

    fun incrementMistakes() {
        mistakes += 1
    }

    fun resetMistakes() {
        mistakes = 0
    }
}