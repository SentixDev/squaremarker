package dev.sentix.squaremarker.forge

import cloud.commandframework.CommandManager
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.neoforge.NeoForgeServerCommandManager
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.BrigadierSetup
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
            SquaremapProvider.get().getWorldIfEnabled(
                WorldIdentifier.parse(level.dimension().location().toString()),
            ).ifPresent(API::initWorld)
        }

        NeoForge.EVENT_BUS.addListener<LevelEvent.Unload> {
            val level = it.level as? ServerLevel ?: return@addListener
            SquaremapProvider.get().getWorldIfEnabled(
                WorldIdentifier.parse(level.dimension().location().toString()),
            ).ifPresent(API::unloadWorld)
        }
    }

    private fun createCommandManager(): CommandManager<Commander> {
        val mgr =
            NeoForgeServerCommandManager(
                CommandExecutionCoordinator.simpleCoordinator(),
                { stack -> ForgeCommander.create(stack) },
                { commander -> (commander as ForgeCommander).sender },
            )
        BrigadierSetup.setup(mgr)
        return mgr
    }
}
