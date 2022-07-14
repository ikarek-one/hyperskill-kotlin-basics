package watermark

import watermark.WatermarkApp.watermarkApp
import kotlin.system.exitProcess

fun main() {
    runApp()
}

fun runApp(runner: IRunner? = null) {
    try {
        if (runner == null)
            watermarkApp()
        else
            watermarkApp(runner)
    } catch (iatExc: InformAndTerminateException) {
        println(iatExc.message)
        if (iatExc.cause != null) {
            throw iatExc.cause
        }
        exitProcess(0)
    }
}

