package watermark

import java.awt.Color
import java.awt.image.BufferedImage

object Watermark {

    fun singleWatermark(
        img: BufferedImage,
        watermark: BufferedImage,
        transformingFun: (Color, Color) -> Color,
        posX: Int,
        posY: Int,
    ): BufferedImage {
        val modified = cloneBufferedImage(img)
        val hasWatermarkAlpha = watermark.colorModel.hasAlpha()

        for (x in 0 until watermark.width) {
            for (y in 0 until watermark.height) {
                val imgX = x + posX
                val imgY = y + posY

                val i = Color(img.getRGB(imgX, imgY))
                val w = Color(watermark.getRGB(x, y), hasWatermarkAlpha)

                val col = transformingFun(i, w)

                modified.setRGB(imgX, imgY, col.rgb)
            }
        }
        return modified
    }

    fun watermarkGrid(
        img: BufferedImage,
        watermark: BufferedImage,
        tranformingFun: (Color, Color) -> Color,
    ): BufferedImage {
        val modified = cloneBufferedImage(img)
        val hasWatermarkAlpha = watermark.colorModel.hasAlpha()

        for (iX in 0 until img.width) {
            for (iY in 0 until img.height) {
                val wX = iX % watermark.width
                val wY = iY % watermark.height

                val i = Color(img.getRGB(iX, iY))
                val w = Color(watermark.getRGB(wX, wY), hasWatermarkAlpha)
                val col = tranformingFun(i, w)

                modified.setRGB(iX, iY, col.rgb)
            }
        }
        return modified
    }

    fun cloneBufferedImage(original: BufferedImage): BufferedImage {
        return BufferedImage(
            original.colorModel,
            original.copyData(null),
            original.colorModel.isAlphaPremultiplied,
            null
        )
    }

}

