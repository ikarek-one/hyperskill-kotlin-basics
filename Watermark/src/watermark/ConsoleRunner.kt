package watermark

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

    println("Input the watermark transparency percentage (Integer 0-100):")
    val transparencyStr = readln()
    val transparency = transparencyToIntOrThrow(transparencyStr)
    checkTransparencyRange(transparency)

    println("Input the output image filename (jpg or png extension):")
    val outputName = readln()
    checkPngJpgExtension(outputName)

    val watermarked = addWatermark(img, watermark, transparency)

    saveImgToFileOrThrow(outputName, watermarked)
    println("The watermarked image $outputName has been created.")
}