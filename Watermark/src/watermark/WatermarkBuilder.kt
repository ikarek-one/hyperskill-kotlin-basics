package watermark

import watermark.Watermark.singleWatermark
import watermark.Watermark.watermarkGrid
import java.awt.Color
import java.awt.image.BufferedImage

class WatermarkBuilder {
    var image: BufferedImage? = null
    var watermark: BufferedImage? = null
    var transparencyColor: Color? = null
    var transparencyPercent: Int? = null
    var positioningType: PositioningType? = null
    var transformingType: ColorTransformingType? = null
    var diffX: Int? = null
    var diffY: Int? = null


    fun build(): BufferedImage {
        if (!isReadyToBuild()) {
            val failMessage = "WatermarkBuilder hasn't got all required properties to build!"
            throw IllegalStateException(failMessage)
        }

        val transformingFun: (Color, Color) -> Color = when (transformingType) {
            ColorTransformingType.ALPHA_CHANNEL ->
                ColorTransformingMethods.usingAlphaChannel(transparencyPercent!!)
            ColorTransformingType.TRANSPARENCY_COLOR ->
                ColorTransformingMethods.usingTransparencyColor(
                    transparencyPercent!!, transparencyColor!!
                )
            ColorTransformingType.SIMPLE ->
                ColorTransformingMethods.simpleTransformation(transparencyPercent!!)
            null ->
                throw AssertionError(
                    "Impossible to happen if readyToBuilt value was properly checked"
                )
        }

        return when (positioningType) {
            PositioningType.SINGLE ->
                singleWatermark(
                    image!!,
                    watermark!!,
                    transformingFun,
                    diffX!!,
                    diffY!!
                )

            PositioningType.GRID ->
                watermarkGrid(
                    image!!,
                    watermark!!,
                    transformingFun
                )
            else -> throw AssertionError(
                "Impossible to happen if readyToBuilt value was properly checked"
            )
        }
    }

    fun isReadyToBuild(): Boolean {
        val req1 = listOf<Any?>(
            image,
            watermark,
            transparencyPercent,
            positioningType,
            transformingType
        ).all { it != null }

        if (!req1)
            return false

        val req2 =
            if (positioningType == PositioningType.SINGLE)
                diffX != null && diffY != null
            else
                true

        if (!req2)
            return false

        val req3 = when (transformingType!!) {
            ColorTransformingType.ALPHA_CHANNEL ->
                ImageChecker.hasAlphaChannel(watermark!!)
            ColorTransformingType.TRANSPARENCY_COLOR ->
                transparencyColor != null
            ColorTransformingType.SIMPLE -> true
        }

        return req3
    }

    fun setImage(image: BufferedImage): WatermarkBuilder {
        this.image = image
        return this
    }

    fun setWatermark(watermark: BufferedImage): WatermarkBuilder {
        this.watermark = watermark
        return this
    }

    fun setTransparencyColor(transparencyColor: Color): WatermarkBuilder {
        this.transparencyColor = transparencyColor
        return this
    }

    fun setTransparencyPercent(transparencyPercent: Int): WatermarkBuilder {
        this.transparencyPercent = transparencyPercent
        return this
    }

    fun setPositionType(positioningType: PositioningType): WatermarkBuilder {
        this.positioningType = positioningType
        return this
    }

    fun setTransformingType(transformingType: ColorTransformingType): WatermarkBuilder {
        this.transformingType = transformingType
        return this
    }

    fun setDiffX(dX: Int): WatermarkBuilder {
        this.diffX = dX
        return this
    }

    fun setDiffY(dY: Int): WatermarkBuilder {
        this.diffY = dY
        return this
    }

}