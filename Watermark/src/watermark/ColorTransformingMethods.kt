package watermark

import java.awt.Color

object ColorTransformingMethods {

    fun usingAlphaChannel(transparencyPercent: Int): (Color, Color) -> Color {
        val ALPHA_TRANSPARENT = 0
        val ALPHA_FILTER = 255

        return { i: Color, w: Color ->
            when (w.alpha) {
                ALPHA_TRANSPARENT -> i
                ALPHA_FILTER -> blendColors(i, w, transparencyPercent)
                else -> i
//                 TODO consider different reaction for situations when w.alpha not in [0, 255]
//                else -> throw IllegalArgumentException(
//                    "Alpha value must be either $ALPHA_TRANSPARENT or $ALPHA_FILTER, found = ${w.alpha}"
//                )
            }
        }
    }

    fun usingTransparencyColor(transparencyPercent: Int, transparencyColor: Color) =
        { i: Color, w: Color ->
            if (w == transparencyColor)
                i
            else
                blendColors(i, w, transparencyPercent)
        }

    fun simpleTransformation(transparencyPercent: Int) =
        { i: Color, w: Color ->
            blendColors(i, w, transparencyPercent)
        }

    private fun blendColors(i: Color, w: Color, weight: Int): Color = Color(
        (weight * w.red + (100 - weight) * i.red) / 100,
        (weight * w.green + (100 - weight) * i.green) / 100,
        (weight * w.blue + (100 - weight) * i.blue) / 100
    )
}
