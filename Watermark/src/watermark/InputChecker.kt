package watermark

import java.awt.Color

fun transparencyToIntOrThrow(strVal: String): Int {
    val failMessage = "The transparency percentage isn't an integer number."
    return strVal.toIntOrNull() ?: throw InformAndTerminateException(failMessage)
}


fun checkTransparencyRange(transparency: Int) {
    if (!(transparency in 0..100)) {
        val failMessage = "The transparency percentage is out of range."
        throw InformAndTerminateException(failMessage)
    }
}


fun checkPngJpgExtension(filename: String) {
    if (!listOf(".jpg", ".png").any { filename.endsWith(it) }) {
        val failMessage = "The output file extension isn't \"jpg\" or \"png\"."
        throw InformAndTerminateException(failMessage)
    }
}

fun lineToColorOrThrow(line: String): Color {
    try {
        val rgbValues = line.split(" ").map { it.toInt() }
        if (rgbValues.size == 3 && rgbValues.asSequence().all { (it in 0..255) }) {
            return Color(rgbValues[0], rgbValues[1], rgbValues[2])
        }
    } catch (nfe: NumberFormatException) {  }

    val failMessage = "The transparency color input is invalid."
    throw InformAndTerminateException(failMessage)
}
