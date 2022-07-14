package watermark

import java.awt.Color
import java.awt.image.BufferedImage

object InputChecker {

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
        } catch (nfe: NumberFormatException) {
        }

        val failMessage = "The transparency color input is invalid."
        throw InformAndTerminateException(failMessage)
    }

    fun choosePositioningMethod(singleOrGridStr: String): PositioningType {
        return when (singleOrGridStr.trim().lowercase()) {
            "single" -> PositioningType.SINGLE
            "grid" -> PositioningType.GRID
            else -> {
                val failMessage = "The position method input is invalid."
                throw InformAndTerminateException(failMessage)
            }
        }
    }

    fun watermarkPositionOrThrow(
        line: String,
        img: BufferedImage,
        watermark: BufferedImage
    ): List<Int> {
        val ints = try {
            line.trim().split(" ").map { it.toInt() }
        } catch (nfe: NumberFormatException) {
            listOf<Int>()
        }

        if (ints.size != 2) {
            val failMessage = "The position input is invalid."
            throw InformAndTerminateException(failMessage)
        }

        if (!
            (ints[0] in 0..(img.width - watermark.width)
                    && (ints[1] in 0..(img.height - watermark.height)))
        ) {
            val failMessage = "The position input is out of range."
            throw InformAndTerminateException(failMessage)
        }

        return ints
    }
}
