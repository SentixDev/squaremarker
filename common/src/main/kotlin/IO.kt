package dev.sentix.squaremarker

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.sentix.squaremarker.marker.Marker
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

object IO {

    private val gsonPrettier: Gson = GsonBuilder().setPrettyPrinting().create()

    private val markerFile = SquareMarker.instance.markerFile

    fun init() {
        val emptyMarkerList = emptyList<Marker>()
        if (!markerFile.exists()) {
            write(emptyMarkerList)
        }
    }

    fun write(input: List<Marker>) {
        if (!markerFile.parent.exists()) {
            markerFile.parent.createDirectories()
        }
        markerFile.bufferedWriter().use { it.write(gsonPrettier.toJson(input)) }
    }

    fun read(): String {
        return markerFile.bufferedReader().use { it.readText() }
    }

}