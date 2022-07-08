// Hyperskill's Parking in Kotlin - stage 5
package parking

fun main() {
    val parking = Parking()

    while (true) {
        val cmd = readln()
        if (cmd.lowercase() == "exit") {
            return
        }
        parking.takeCommand(cmd)
    }
}


