package watermark

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

fun main() {
    stage2()
}

fun stage2() {
    try {
        runApp()
    } catch (iatExc: InformAndTerminateException) {
        println(iatExc.message)
        if (iatExc.cause != null) {
            throw iatExc.cause
        }
    }
}

// from stage 1, but kept
fun getImageInfoFromFile(filename: String): String {
    val file = File(filename)
    return try {
        val img = ImageIO.read(file)
        "Image file: $filename \n" + getImageInfo(img)
    } catch (ioe: IOException) {
        "The file $filename doesn't exist."
    }
}

fun getImageInfo(img: BufferedImage): String {
    return """
    Width: ${img.width}
    Height: ${img.height}
    Number of components: ${img.colorModel.numComponents}
    Number of color components: ${img.colorModel.numColorComponents}
    Bits per pixel: ${img.colorModel.pixelSize}
    Transparency: ${
        when (img.transparency) {
            1 -> "OPAQUE"
            2 -> "BITMASK"
            3 -> "TRANSLUCENT"
            else -> throw IllegalArgumentException(
                "tranparency = ${img.transparency} not in 1..3"
            )
        }
    }    
    """.trimIndent()
}
