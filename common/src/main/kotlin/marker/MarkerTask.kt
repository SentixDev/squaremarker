package dev.sentix.squaremarker.marker

import dev.sentix.squaremarker.SquareMarker
import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.MapWorld
import xyz.jpenilla.squaremap.api.Point
import xyz.jpenilla.squaremap.api.SimpleLayerProvider
import xyz.jpenilla.squaremap.api.marker.Icon
import xyz.jpenilla.squaremap.api.marker.Marker
import xyz.jpenilla.squaremap.api.marker.MarkerOptions

class MarkerTask(world: MapWorld, provider: SimpleLayerProvider) : Runnable {

    private val world: MapWorld
    private val provider: SimpleLayerProvider
    private var stop = false

    override fun run() {

        if (stop) {
            API.cancel(world.identifier())
        }

        provider.clearMarkers()
        MarkerService.getMarkerList().forEach { marker ->
            if (marker.world == world.identifier()) {
                val iconKey: Key = if (marker.iconUrl != "") {
                    Key.of(marker.iconKey)
                } else {
                    API.markerIconKey
                }
                handle(
                    marker.id,
                    marker.content,
                    iconKey,
                    marker.posX,
                    marker.posZ
                )
            }

        }

    }

    private fun handle(id: Int, name: String, iconKey: Key, x: Double, z: Double) {
        val icon: Icon = Marker.icon(Point.point(x, z), iconKey, SquareMarker.instance.config.iconSize)
        if (name.isNotBlank()) {
            icon.markerOptions(
                MarkerOptions.builder()
                    .hoverTooltip(
                        "<center>$name</center>"
                    )
            )
        }
        val markerId = "squaremarker_marker_$id"
        provider.addMarker(Key.of(markerId), icon)
    }

    fun disable() {
        stop = true
        provider.clearMarkers()
    }

    init {
        this.world = world
        this.provider = provider
    }

}