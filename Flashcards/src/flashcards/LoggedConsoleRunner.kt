package flashcards

class LoggedConsoleRunner : IRunner {
    private val lines = mutableListOf<LogLine>()

    override fun ask(): String {
        val line = readln()
        lines.add(LogLine(line, LogLine.Type.INPUT))
        return line
    }

    override fun say(line: String) {
        lines.add(LogLine(line, LogLine.Type.OUTPUT))
        println(line)
    }

    override fun log(): String {
        return lines.joinToString("\n") { it.line }
    }

}

data class LogLine(val line: String, val type: Type) {
    enum class Type {
        INPUT,
        OUTPUT
    }
}
