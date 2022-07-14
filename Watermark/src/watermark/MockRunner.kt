package watermark
/**
 * Class used for tests. Enables to seed program with mocking input without typing it from the console.
 * Prints input and output to the console
 * @param input text that is going to be entered to the program, line by line
 * @param filter if (true) then takes only lines starting with '>' from input to insert to the program
 * */
class MockRunner(val input: String, filter: Boolean = false) : IRunner {
    private val iterator =
        if (filter)
            input.split("\n")
                .filter { it.trim().startsWith(">") }
                .map { it.drop(1).trim() }
                .iterator()
        else
            input.split("\n")
                .iterator()


    override fun ask(): String {
        if (!iterator.hasNext()) {
            throw IllegalStateException(
                "Mock iterator ran out of lines; too much input was expected"
            )
        }
        val line = iterator.next()
        println(">>>   $line")
        return line
    }

    override fun say(line: String) {
        println(line)
    }

}