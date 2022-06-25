package dev.sentix.squaremarker.fabric

import cloud.commandframework.CommandManager
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.fabric.FabricServerCommandManager
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.BrigadierSetup
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.fabric.command.FabricCommander
import dev.sentix.squaremarker.marker.API
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.minecraft.resources.ResourceLocation
import xyz.jpenilla.squaremap.api.SquaremapProvider
import xyz.jpenilla.squaremap.api.WorldIdentifier

class SquareMarkerInitializer : ModInitializer {
    companion object {
        val modContainer: ModContainer = FabricLoader.getInstance().getModContainer("squaremarker").orElseThrow()
    }

    private val squareMarker: SquareMarker = SquareMarker(
        createCommandManager(),
        FabricLoader.getInstance().configDir.resolve("${modContainer.metadata.id}.yml"),
        FabricLoader.getInstance().gameDir.resolve(modContainer.metadata.id)
    )

    override fun onInitialize() {
        ServerLifecycleEvents.SERVER_STOPPED.register { squareMarker.shutdown() }

        // Use custom late phase as workaround for squaremap <1.1.7
        val late = ResourceLocation("squaremarker:late")
        ServerWorldEvents.LOAD.register(late) { _, world ->
            SquaremapProvider.get().getWorldIfEnabled(
                WorldIdentifier.parse(world.dimension().location().toString())
            ).ifPresent(API::initWorld)
        }
        ServerWorldEvents.LOAD.addPhaseOrdering(Event.DEFAULT_PHASE, late)

        ServerWorldEvents.UNLOAD.register { _, world ->
            SquaremapProvider.get().getWorldIfEnabled(
                WorldIdentifier.parse(world.dimension().location().toString())
            ).ifPresent(API::unloadWorld)
        }
    }

    private fun createCommandManager(): CommandManager<Commander> {
        val mgr = FabricServerCommandManager(
            CommandExecutionCoordinator.simpleCoordinator(),
            { stack -> FabricCommander.create(stack) },
            { commander -> (commander as FabricCommander).sender }
        )
        BrigadierSetup.setup(mgr)
        return mgr
    }
}
