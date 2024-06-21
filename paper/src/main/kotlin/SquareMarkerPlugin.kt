package dev.sentix.squaremarker.paper

import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.marker.API
import dev.sentix.squaremarker.paper.command.PaperCommander
import org.bstats.bukkit.Metrics
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager
import xyz.jpenilla.squaremap.api.BukkitAdapter
import xyz.jpenilla.squaremap.api.SquaremapProvider

class SquareMarkerPlugin : JavaPlugin(), Listener {
    private lateinit var squareMarker: SquareMarker

    override fun onEnable() {
        squareMarker =
            SquareMarker(
                createCommandManager(),
                dataFolder.toPath().resolve("config.yml"),
                dataFolder.toPath(),
                PaperWorldIdentifierSerializer(server),
            ).also { it.init() }

        server.pluginManager.registerEvents(this, this)

        // https://bstats.org/plugin/bukkit/squaremarker/14117
        Metrics(this, 14117)
    }

    override fun onDisable() {
        squareMarker.shutdown()
    }

    private fun createCommandManager(): LegacyPaperCommandManager<Commander> {
        return LegacyPaperCommandManager(
            this,
            ExecutionCoordinator.builder<Commander>()
                .synchronizeExecution(folia)
                .build(),
            SenderMapper.create(
                { sender -> PaperCommander.create(sender) },
                { commander -> (commander as PaperCommander).sender },
            ),
        )
    }

    @EventHandler
    fun handleLoad(event: WorldLoadEvent) {
        SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(event.world)).ifPresent(API::initWorld)
    }

    @EventHandler
    fun handleunLoad(event: WorldLoadEvent) {
        SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(event.world)).ifPresent(API::unloadWorld)
    }
}
