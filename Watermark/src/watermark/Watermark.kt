package watermark

import java.awt.Color
import java.awt.image.BufferedImage

fun addWatermark(
    img: BufferedImage, watermark: BufferedImage, transparency: Int
): BufferedImage {
//    if (img.type != watermark.type) {
//        // TODO learn about how to convert different types (see BufferedImage.TYPE_INT_RGB)
//        val failMessage = "Image and watermark has different RGB encoding types!"
//        throw InformAndTerminateException(failMessage)
//    }

    val watermarked = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_RGB)

    for (x in 0 until img.width) {
        for (y in 0 until img.height) {
            val i = Color(img.getRGB(x, y))
            val w = Color(watermark.getRGB(x, y))
            val col = convertColor(i, w, transparency)
            watermarked.setRGB(x, y, col.rgb)
        }
    }

    return watermarked
}

fun convertColor(i: Color, w: Color, weight: Int): Color = Color(
    (weight * w.red + (100 - weight) * i.red) / 100,
    (weight * w.green + (100 - weight) * i.green) / 100,
    (weight * w.blue + (100 - weight) * i.blue) / 100
)
