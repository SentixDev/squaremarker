package dev.sentix.squaremarker.marker

import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.Config
import dev.sentix.squaremarker.Lang
import dev.sentix.squaremarker.SquareMarker
import org.bukkit.Bukkit
import xyz.jpenilla.squaremap.api.BukkitAdapter
import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.SimpleLayerProvider
import xyz.jpenilla.squaremap.api.SquaremapProvider
import java.net.URL
import javax.imageio.ImageIO

object API {

    val markerIconKey: Key = Key.of("squaremarker_marker_icon_")

    private val providerMap: MutableMap<String, MarkerTask> = HashMap()

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
            SquareMarker.main.let { task.runTaskTimerAsynchronously(it, 0, 20L * 2) }
            providerMap[BukkitAdapter.bukkitWorld(mapWorld).uid.toString()] = task
        }

    }

    private fun registerIcons() {
        SquaremapProvider.get().iconRegistry()
            .register(markerIconKey, ImageIO.read(URL(Config.ICON_URL)))
        for (marker in MarkerService.getMarkerList()) {
            println(marker.iconUrl)
            if (marker.iconUrl.isNotBlank()) {
                try {
                    SquaremapProvider.get().iconRegistry()
                        .register(Key.of("squaremarker_marker_icon_${marker.id}"), ImageIO.read(URL(marker.iconUrl)))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Components.send(Bukkit.getConsoleSender(), "")
                    Components.send(Bukkit.getConsoleSender(), "${Lang.PREFIX} There is an invalid url in your marker.json. Please fix \"${marker.iconUrl}\"!")
                    Components.send(Bukkit.getConsoleSender(), "")
                }
            }
        }
    }

    fun unregister() {
        providerMap.values.forEach(MarkerTask::disable)
        providerMap.clear()
    }

}