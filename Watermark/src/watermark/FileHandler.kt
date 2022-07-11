package watermark

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


fun fileToImgOrThrow(filename: String): BufferedImage {
    try {
        val file = File(filename)
        return ImageIO.read(file)
    } catch (ioe: IOException) {
        val failMessage = "The file $filename doesn't exist."
        throw InformAndTerminateException(failMessage)
    }
}


fun saveImgToFileOrThrow(filename: String, img: BufferedImage) {
    try {
//        if (filename.contains('\\')) {
//            val directoryPath = filename.substringBeforeLast('\\')
//            val directoryFile = File(directoryPath)
//            if (!directoryFile.exists()) {
//                directoryFile.mkdirs()
//            }
//        }

        val outputFile = File(filename)
        val wasSaved = ImageIO.write(img, filename.takeLast(3), outputFile)
        if (wasSaved && outputFile.exists()) return
    } catch (ioe: IOException) {
        val failMessage = "Unable to save the file named $filename"
        throw InformAndTerminateException(failMessage, ioe)
    }
    val failMessage = "Unable to save the file named $filename"
    throw InformAndTerminateException(failMessage)
}
