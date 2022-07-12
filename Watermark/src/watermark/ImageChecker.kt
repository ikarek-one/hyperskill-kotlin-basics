package watermark

import java.awt.image.BufferedImage

fun checkNumOfColorComponents(bufImg: BufferedImage, typeOfImg: String) {
    if (bufImg.colorModel.numColorComponents != 3) {
        val failMessage = "The number of $typeOfImg color components isn't 3."
        throw InformAndTerminateException(failMessage)
    }
}


fun checkNumOfBits(bufImg: BufferedImage, typeOfImg: String) {
    if (!(bufImg.colorModel.pixelSize in listOf(24, 32))) {
        val failMessage = "The $typeOfImg isn't 24 or 32-bit."
        throw InformAndTerminateException(failMessage)
    }
}


fun checkIfDimensionsEqual(bufImg1: BufferedImage, bufImg2: BufferedImage) {
    if ((bufImg1.height != bufImg2.height) || (bufImg1.width != bufImg2.width)) {
        val failMessage = "The image and watermark dimensions are different."
        throw InformAndTerminateException(failMessage)
    }
}

fun isTranslucent(bufImg: BufferedImage): Boolean =
    bufImg.transparency == BufferedImage.TRANSLUCENT

fun hasAlphaChannel(bufImg: BufferedImage): Boolean =
    bufImg.colorModel.hasAlpha()
