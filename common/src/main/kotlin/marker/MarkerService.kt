package dev.sentix.squaremarker.marker

import com.google.gson.reflect.TypeToken
import dev.sentix.squaremarker.IO

object MarkerService {

    fun getMarkerList() : MutableList<Marker> {
        val type = object : TypeToken<MutableList<Marker>>(){}.type
        return IO.gson.fromJson(IO.read(), type)
    }

    fun getMarker(id: Int): Marker {
        return getMarkerList().filter { it.id == id }[0]
    }

    fun markerExist(id: Int): Boolean {
        return try {
            getMarker(id)
            true
        } catch (_: IndexOutOfBoundsException) {
            false
        }
    }

    fun addMarker(marker: Marker) {
        val markerList = getMarkerList()
        markerList.add(marker)
        IO.write(markerList)
    }

    fun removeMarker(id: Int) {
        val markerList = getMarkerList()
        markerList.filter { it.id != id }.toMutableList().let { it1 -> IO.write(it1) }
    }

    fun updateMarker(marker: Marker) {
        removeMarker(marker.id)
        addMarker(marker)
    }

}