package dev.sentix.squaremarker

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.sentix.squaremarker.marker.Marker
import java.io.File

object IO {

    private val gsonPrettier: Gson = GsonBuilder().setPrettyPrinting().create()

    private val markerFile = File(Config.MARKER_FILE)

    fun init() {
        val emptyMarkerList = emptyList<Marker>()
        if (!markerFile.exists()) {
            markerFile.bufferedWriter().use { it.write(gsonPrettier.toJson(emptyMarkerList)) }
        }
    }

    fun write(input: MutableList<Marker>) {
        markerFile.bufferedWriter().use { it.write(gsonPrettier.toJson(input)) }
    }

    fun read(): String {
        return markerFile.bufferedReader().use { it.readText() }
    }

}