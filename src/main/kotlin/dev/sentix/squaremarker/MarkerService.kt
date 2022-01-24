package dev.sentix.squaremarker

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MarkerService {

    fun getMarkerList() : MutableList<Marker>? {
        val type = object : TypeToken<MutableList<Marker>>(){}.type
        return Gson().fromJson(IO.read(), type)
    }

    fun getMarker(id: Int): Marker? {
        return getMarkerList()?.filter { it -> it.id == id }?.get(0)
    }

    fun addMarker(marker: Marker) {
        val markerList = getMarkerList()
        if (markerList != null) {
            markerList.add(marker)
            IO.write(markerList)
        }
    }

    fun removeMarker(id: Int) {
        val markerList = getMarkerList()
        markerList?.filter { it -> it.id != id }?.toMutableList()?.let { it1 -> IO.write(it1) }
    }

    fun updateMarker(marker: Marker) {
        removeMarker(marker.id)
        addMarker(marker)
    }

}