package dev.sentix.squaremarker.forge

import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.forge.command.ForgeCommander
import dev.sentix.squaremarker.marker.API
import net.minecraft.server.level.ServerLevel
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.level.LevelEvent
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.neoforged.neoforge.event.server.ServerStoppedEvent
import org.incendo.cloud.CommandManager
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.neoforge.NeoForgeServerCommandManager
import xyz.jpenilla.squaremap.api.SquaremapProvider
import xyz.jpenilla.squaremap.api.WorldIdentifier
import java.io.File
import java.nio.file.Path

@Mod("squaremarker")
class SquareMarkerInitializer(
    modContainer: ModContainer,
) {
    private val dir: Path = File(modContainer.modId).toPath()
    private val squareMarker: SquareMarker =
        SquareMarker(
            createCommandManager(),
            dir.resolve("config.yml"),
            dir,
        )

    init {
        // Ensure we initialize after squaremap regardless of load order (future squaremap versions should provide a better mechanism)
        NeoForge.EVENT_BUS.addListener<ServerStartingEvent> { squareMarker.init() }
        NeoForge.EVENT_BUS.addListener<ServerStoppedEvent> { squareMarker.shutdown() }

        NeoForge.EVENT_BUS.addListener<LevelEvent.Load> {
            val level = it.level as? ServerLevel ?: return@addListener
            SquaremapProvider
                .get()
                .getWorldIfEnabled(
                    WorldIdentifier.parse(level.dimension().location().toString()),
                ).ifPresent(API::initWorld)
        }

        NeoForge.EVENT_BUS.addListener<LevelEvent.Unload> {
            val level = it.level as? ServerLevel ?: return@addListener
            SquaremapProvider
                .get()
                .getWorldIfEnabled(
                    WorldIdentifier.parse(level.dimension().location().toString()),
                ).ifPresent(API::unloadWorld)
        }
    }

    private fun createCommandManager(): CommandManager<Commander> {
        val mgr =
            NeoForgeServerCommandManager(
                ExecutionCoordinator.simpleCoordinator(),
                SenderMapper.create(
                    { stack -> ForgeCommander.create(stack) },
                    { commander -> (commander as ForgeCommander).sender },
                ),
            )
        return mgr
    }
}
