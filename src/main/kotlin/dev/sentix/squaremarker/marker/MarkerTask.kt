package dev.sentix.squaremarker.marker

import dev.sentix.squaremarker.Config
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import xyz.jpenilla.squaremap.api.*
import xyz.jpenilla.squaremap.api.marker.Icon
import xyz.jpenilla.squaremap.api.marker.Marker
import xyz.jpenilla.squaremap.api.marker.MarkerOptions

class MarkerTask(world: MapWorld, provider: SimpleLayerProvider) : BukkitRunnable() {

    private val world: MapWorld
    private val provider: SimpleLayerProvider
    private var stop = false

    override fun run() {

        if (stop) {
            cancel()
        }

        provider.clearMarkers()
        MarkerService.getMarkerList().forEach { marker ->
            if (Bukkit.getWorld(marker.world)?.uid == BukkitAdapter.bukkitWorld(world).uid) {
                val iconKey: Key = if (marker.iconUrl != "") {
                    Key.of(marker.iconKey)
                } else {
                    API.markerIconKey
                }
                handle(
                    marker.id,
                    marker.content,
                    iconKey,
                    Location(
                        Bukkit.getWorld(marker.world),
                        marker.posX,
                        marker.posY,
                        marker.posY,
                        0.0F,
                        0.0F
                    )
                )
            }

        }

    }

    private fun handle(id: Int, name: String, iconKey: Key, location: Location) {
        val worldName = location.world.name
        val icon: Icon = Marker.icon(BukkitAdapter.point(location), iconKey, Config.ICON_SIZE)
        if (name.isNotEmpty()) {
            icon.markerOptions(
                MarkerOptions.builder()
                    .hoverTooltip(
                        "<center>$name</center>"
                    )
            )
        }
        val markerid = "squaremarker_" + worldName + "_marker_" + id
        provider.addMarker(Key.of(markerid), icon)
    }

    fun disable() {
        cancel()
        stop = true
        provider.clearMarkers()
    }

    init {
        this.world = world
        this.provider = provider
    }

}