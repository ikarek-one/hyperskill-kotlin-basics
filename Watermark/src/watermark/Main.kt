package watermark

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

fun main() {
    stage3()
}

fun stage3() {
    try {
        runApp()
    } catch (iatExc: InformAndTerminateException) {
        println(iatExc.message)
        if (iatExc.cause != null) {
            throw iatExc.cause
        }
    }
}

