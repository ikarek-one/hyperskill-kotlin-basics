package watermark

import java.awt.Color

fun runApp() {
    println("Input the image filename:")
    val imgName = readln()
    val img = fileToImgOrThrow(imgName)
    checkNumOfColorComponents(img, "image")
    checkNumOfBits(img, "image")

    println("Input the watermark image filename:")
    val watermarkName = readln()
    val watermark = fileToImgOrThrow(watermarkName)
    checkNumOfColorComponents(watermark, "watermark")
    checkNumOfBits(watermark, "watermark")

    checkIfDimensionsEqual(img, watermark)

    val usingTransparencyColor = if (!hasAlphaChannel(watermark)) {
        println("Do you want to set a transparency color?")
        readln().trim().lowercase().equals("yes")
    } else {
        false
    }

    val transparencyColor: Color? =
        if (usingTransparencyColor) {
            println("Input a transparency color ([Red] [Green] [Blue]):")
            val colorLine = readln()
            lineToColorOrThrow(colorLine)
        } else {
            null
        }

    val usingAlphaChannel: Boolean =
        if (!usingTransparencyColor && isTranslucent(watermark)) {
            println("Do you want to use the watermark's Alpha channel?")
            readln().trim().lowercase().equals("yes")
        } else {
            false
        }

    println("Input the watermark transparency percentage (Integer 0-100):")
    val transparencyStr = readln()
    val transparency = transparencyToIntOrThrow(transparencyStr)
    checkTransparencyRange(transparency)

    println("Input the output image filename (jpg or png extension):")
    val outputName = readln()
    checkPngJpgExtension(outputName)

    val watermarked =
        addWatermark(img, watermark, transparency, usingAlphaChannel, transparencyColor)

    saveImgToFileOrThrow(outputName, watermarked)
    println("The watermarked image $outputName has been created.")
}