package dev.sentix.squaremarker.marker

import dev.sentix.squaremarker.SquareMarker
import java.awt.image.BufferedImage
import java.io.File
import java.net.URI
import javax.imageio.ImageIO

object ImageLoader {
    fun load(iconUrl: String): BufferedImage {
        return if (iconUrl.startsWith("http://") || iconUrl.startsWith("https://")) {
            ImageIO.read(URI(iconUrl).toURL())
                ?: throw IllegalArgumentException("Could not read image from URL: $iconUrl")
        } else {
            val file = resolveFilePath(iconUrl)
            if (!file.exists() || !file.isFile) {
                throw IllegalArgumentException("File not found: ${file.absolutePath}")
            }
            ImageIO.read(file)
                ?: throw IllegalArgumentException("Could not read image from file: ${file.absolutePath}")
        }
    }

    private fun resolveFilePath(path: String): File {
        val file = File(path)
        if (file.isAbsolute || path.startsWith("plugins/") || path.startsWith("./") || path.startsWith("../")) {
            return file
        }
        return SquareMarker.instance.dataDir.resolve(path).toFile()
    }
}

