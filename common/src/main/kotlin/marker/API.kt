package dev.sentix.squaremarker.marker

import dev.sentix.squaremarker.Lang
import dev.sentix.squaremarker.SquareMarker
import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.MapWorld
import xyz.jpenilla.squaremap.api.SimpleLayerProvider
import xyz.jpenilla.squaremap.api.SquaremapProvider
import xyz.jpenilla.squaremap.api.WorldIdentifier
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object API {
    val markerIconKey: Key = Key.of("squaremarker_marker_icon_")

    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val providerMap: MutableMap<WorldIdentifier, Pair<ScheduledFuture<*>, MarkerTask>> = HashMap()

    fun init() {
        registerIcons()

        SquaremapProvider.get().mapWorlds().forEach(::initWorld)
    }

    fun unloadWorld(mapWorld: MapWorld) {
        providerMap.remove(mapWorld.identifier())?.apply {
            second.disable()
            first.cancel(true)
        }
    }

    fun initWorld(mapWorld: MapWorld) {
        registerIcons()

        if (mapWorld.identifier() in providerMap) {
            return
        }

        val provider: SimpleLayerProvider =
            SimpleLayerProvider
                .builder(SquareMarker.instance.config.layerName)
                .apply {
                    defaultHidden(SquareMarker.instance.config.defaultHidden)
                    showControls(SquareMarker.instance.config.showControls)
                }.build()
        val key = Key.of("squaremarker_marker")
        if (mapWorld.layerRegistry().hasEntry(key)) {
            mapWorld.layerRegistry().unregister(key)
        }
        mapWorld.layerRegistry().register(key, provider)
        val task = MarkerTask(mapWorld, provider)
        val scheduled =
            executor.scheduleAtFixedRate(
                task,
                0,
                SquareMarker.instance.config.updateRateMilliseconds,
                TimeUnit.MILLISECONDS,
            )
        providerMap[mapWorld.identifier()] = Pair(scheduled, task)
    }

    fun registerIcons(onlyIfMissing: Boolean = false) {
        val iconRegistry = SquaremapProvider.get().iconRegistry()

        // Register the default icon.
        if (!onlyIfMissing || !iconRegistry.hasEntry(markerIconKey)) {
            try {
                iconRegistry.register(
                    markerIconKey,
                    ImageLoader.load(SquareMarker.instance.config.iconUrl),
                )
            } catch (ex: Exception) {
                SquareMarker.logger.warn(
                    "${Lang.PLAIN_PREFIX} Could not load the default icon from \"${SquareMarker.instance.config.iconUrl}\".",
                    ex,
                )
            }
        }

        for (marker in MarkerService.getMarkerList()) {
            if (marker.iconUrl.isNotBlank()) {
                val key = Key.of("squaremarker_marker_icon_${marker.id}")
                if (!onlyIfMissing || !iconRegistry.hasEntry(key)) {
                    try {
                        iconRegistry.register(key, ImageLoader.load(marker.iconUrl))
                    } catch (ex: Exception) {
                        SquareMarker.logger.warn(
                            "${Lang.PLAIN_PREFIX} There is an invalid url in your marker.json. Please fix \"${marker.iconUrl}\"!",
                            ex,
                        )
                    }
                }
            }
        }
    }

    fun cancel(world: WorldIdentifier) {
        providerMap[world]?.first?.cancel(true)
    }

    fun unregister() {
        providerMap.values.forEach { (future, task) ->
            task.disable()
            future.cancel(true)
        }
        providerMap.clear()
        executor.shutdownNow()
    }
}
