package dev.sentix.squaremarker.marker

import dev.sentix.squaremarker.Config
import dev.sentix.squaremarker.SquareMarker
import xyz.jpenilla.squaremap.api.BukkitAdapter
import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.SimpleLayerProvider
import xyz.jpenilla.squaremap.api.SquaremapProvider
import java.net.URL
import javax.imageio.ImageIO

object API {

    val markerIconKey: Key = Key.of("squaremarker_marker_icon_")

    val providerMap: MutableMap<String, MarkerTask> = HashMap()

    fun init() {

        registerIcons()

        SquaremapProvider.get().mapWorlds().forEach { mapWorld ->
            val provider: SimpleLayerProvider = SimpleLayerProvider.builder(Config.LAYER_NAME)
                .showControls(true)
                .defaultHidden(false)
                .build()
            mapWorld.layerRegistry()
                .register(Key.of("squaremarker_" + BukkitAdapter.bukkitWorld(mapWorld).uid + "_marker"), provider)
            val task = MarkerTask(mapWorld, provider)
            SquareMarker.main.let { task.runTaskTimerAsynchronously(it, 0, 20L * 3) }
            providerMap[BukkitAdapter.bukkitWorld(mapWorld).uid.toString()] = task
        }
    }

    private fun registerIcons() {
        SquaremapProvider.get().iconRegistry()
            .register(markerIconKey, ImageIO.read(URL(Config.ICON_URL)))
        for (marker in MarkerService.getMarkerList()) {
            if (marker.iconUrl != "") {
                SquaremapProvider.get().iconRegistry()
                    .register(Key.of("squaremarker_marker_icon_${marker.id}"), ImageIO.read(URL(marker.iconUrl)))
            }
        }
    }

    fun unregister() {
        providerMap.values.forEach(MarkerTask::disable)
        providerMap.clear()
    }


}