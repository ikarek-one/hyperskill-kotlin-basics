package watermark

import java.awt.Color
import java.awt.image.BufferedImage

fun addWatermark(
    img: BufferedImage,
    watermark: BufferedImage,
    transparency: Int,
    usingAlphaChannel: Boolean = false
): BufferedImage {
    val watermarked = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_RGB)

    for (x in 0 until img.width) {
        for (y in 0 until img.height) {
            val i = Color(img.getRGB(x, y), usingAlphaChannel)
            val w = Color(watermark.getRGB(x, y), usingAlphaChannel)
            val col = if (usingAlphaChannel && w.alpha == 0) {
                i
            } else if (usingAlphaChannel && w.alpha != 255) {
                throw Exception(
                    "Alpha channel of pixel (x = $x, y = $y) has alpha val = ${w.alpha} not in [0,255]"
                )
            } else {
                convertColor(i, w, transparency)
            }
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
