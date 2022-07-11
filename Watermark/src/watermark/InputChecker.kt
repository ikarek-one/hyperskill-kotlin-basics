package watermark

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

