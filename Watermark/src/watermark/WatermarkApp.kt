package watermark

import watermark.FileHandler.fileToImgOrThrow
import watermark.FileHandler.saveImgToFileOrThrow
import watermark.ImageChecker.checkIfNotLarger
import watermark.ImageChecker.checkNumOfBits
import watermark.ImageChecker.checkNumOfColorComponents
import watermark.ImageChecker.hasAlphaChannel
import watermark.ImageChecker.isTranslucent
import watermark.InputChecker.checkPngJpgExtension
import watermark.InputChecker.checkTransparencyRange
import watermark.InputChecker.lineToColorOrThrow
import watermark.InputChecker.transparencyToIntOrThrow
import watermark.InputChecker.watermarkPositionOrThrow

object WatermarkApp {

    fun watermarkApp(runner: IRunner = ConsoleRunner()) {
        val builder = WatermarkBuilder()

        runner.say("Input the image filename:")
        val imgName = runner.ask()
        val img = fileToImgOrThrow(imgName)
        builder.setImage(img)

        checkNumOfColorComponents(img, "image")
        checkNumOfBits(img, "image")

        runner.say("Input the watermark image filename:")
        val watermarkName = runner.ask()
        val watermark = fileToImgOrThrow(watermarkName)
        builder.setWatermark(watermark)

        checkNumOfColorComponents(watermark, "watermark")
        checkNumOfBits(watermark, "watermark")

        checkIfNotLarger(img, watermark)

        // setting default TransformingType
        builder.setTransformingType(ColorTransformingType.SIMPLE)

        if (!hasAlphaChannel(watermark)) {
            runner.say("Do you want to set a transparency color?")

            if (runner.ask().trim().lowercase().equals("yes")) {
                runner.say("Input a transparency color ([Red] [Green] [Blue]):")
                val colorLine = runner.ask()
                builder.setTransparencyColor(lineToColorOrThrow(colorLine))
                builder.setTransformingType(ColorTransformingType.TRANSPARENCY_COLOR)
            }
        } else if (isTranslucent(watermark)) {
            runner.say("Do you want to use the watermark's Alpha channel?")
            if (runner.ask().trim().lowercase().equals("yes")) {
                builder.setTransformingType(ColorTransformingType.ALPHA_CHANNEL)
            }
        }

        runner.say("Input the watermark transparency percentage (Integer 0-100):")
        val transparencyStr = runner.ask()
        val transparencyPercent = transparencyToIntOrThrow(transparencyStr)
        checkTransparencyRange(transparencyPercent)
        builder.setTransparencyPercent(transparencyPercent)

        runner.say("Choose the position method (single, grid):")
        val singleOrGrid = InputChecker.choosePositioningMethod(runner.ask())
        builder.setPositionType(singleOrGrid)

        if (singleOrGrid == PositioningType.SINGLE) {
            val dX = img.width - watermark.width
            val dY = img.height - watermark.height
            runner.say("Input the watermark position ([x 0-$dX] [y 0-$dY]):")
            val positionLine = runner.ask()
            val (diffX, diffY) = watermarkPositionOrThrow(positionLine, img, watermark)
            builder.setDiffX(diffX)
            builder.setDiffY(diffY)
        }

        runner.say("Input the output image filename (jpg or png extension):")
        val outputName = runner.ask()
        checkPngJpgExtension(outputName)

        val watermarked =
            builder.build()

        saveImgToFileOrThrow(outputName, watermarked)
        runner.say("The watermarked image $outputName has been created.")
    }

}

