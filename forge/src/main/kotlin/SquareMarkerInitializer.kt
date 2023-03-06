package dev.sentix.squaremarker.forge

import cloud.commandframework.CommandManager
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.forge.ForgeServerCommandManager
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.BrigadierSetup
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.forge.command.ForgeCommander
import dev.sentix.squaremarker.marker.API
import net.minecraft.server.level.ServerLevel
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.level.LevelEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.event.server.ServerStoppedEvent
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.Mod
import xyz.jpenilla.squaremap.api.SquaremapProvider
import xyz.jpenilla.squaremap.api.WorldIdentifier
import java.io.File
import java.nio.file.Path

@Mod("squaremarker")
class SquareMarkerInitializer {
    private val modContainer: ModContainer = ModList.get().getModContainerById("squaremarker").orElseThrow()
    private val dir: Path = File(modContainer.modId).toPath()
    private val squareMarker: SquareMarker = SquareMarker(
        createCommandManager(),
        dir.resolve("config.yml"),
        dir
    )

    init {
        // Ensure we initialize after squaremap regardless of load order (future squaremap versions should provide a better mechanism)
        MinecraftForge.EVENT_BUS.addListener<ServerStartingEvent> { squareMarker.init() }
        MinecraftForge.EVENT_BUS.addListener<ServerStoppedEvent> { squareMarker.shutdown() }

        MinecraftForge.EVENT_BUS.addListener<LevelEvent.Load> {
            val level = it.level as? ServerLevel ?: return@addListener
            SquaremapProvider.get().getWorldIfEnabled(
                WorldIdentifier.parse(level.dimension().location().toString())
            ).ifPresent(API::initWorld)
        }

        MinecraftForge.EVENT_BUS.addListener<LevelEvent.Unload> {
            val level = it.level as? ServerLevel ?: return@addListener
            SquaremapProvider.get().getWorldIfEnabled(
                WorldIdentifier.parse(level.dimension().location().toString())
            ).ifPresent(API::unloadWorld)
        }
    }

    private fun createCommandManager(): CommandManager<Commander> {
        val mgr = ForgeServerCommandManager(
            CommandExecutionCoordinator.simpleCoordinator(),
            { stack -> ForgeCommander.create(stack) },
            { commander -> (commander as ForgeCommander).sender }
        )
        BrigadierSetup.setup(mgr)
        return mgr
    }
}
