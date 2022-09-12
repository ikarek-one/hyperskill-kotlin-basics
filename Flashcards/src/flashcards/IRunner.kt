package flashcards

interface IRunner {
    /**
     * Returns the next line of program input
     * @throws IllegalStateException if the line is not available
     * */
    fun ask(): String

    /**
     * Consumes a line of program output and forwards it to the console or other output medium.
     * */
    fun say(line: String)

    /**
     * Returns a log - all the lines used as input or output
     * Returns null if log is not available
     */
    fun log(): String? = null
}
