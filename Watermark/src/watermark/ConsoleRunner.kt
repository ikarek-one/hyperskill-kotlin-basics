package watermark

class ConsoleRunner : IRunner {
    override fun ask(): String {
        return readln()
    }

    override fun say(line: String) {
        println(line)
    }

}