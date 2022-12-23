package dev.sentix.squaremarker

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.sentix.squaremarker.marker.Marker
import xyz.jpenilla.squaremap.api.WorldIdentifier
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

object IO {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(WorldIdentifier::class.java, SquareMarker.instance.worldIdentifierSerializer)
        .create()

    private val gsonPrettier: Gson = gson.newBuilder()
        .setPrettyPrinting()
        .create()

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
