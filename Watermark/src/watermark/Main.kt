package watermark

import kotlin.system.exitProcess

fun main() {
    stage4()
}

fun stage4() {
    try {
        runApp()
    } catch (iatExc: InformAndTerminateException) {
        println(iatExc.message)
        if (iatExc.cause != null) {
            throw iatExc.cause
        }
        exitProcess(0)
    }
}

