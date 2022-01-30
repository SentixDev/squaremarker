package dev.sentix.squaremarker.command.commands

import cloud.commandframework.arguments.standard.IntegerArgument
import cloud.commandframework.context.CommandContext
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys
import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.CommandManager
import dev.sentix.squaremarker.command.SquaremarkerCommand
import dev.sentix.squaremarker.marker.MarkerService
import org.bukkit.command.CommandSender

class RemoveMarkerCommand(plugin: SquareMarker, commandManager: CommandManager) :
    SquaremarkerCommand(
        plugin,
        commandManager
    ) {

    override fun register() {
        this.commandManager.registerSubcommand { builder ->
            builder.literal("remove")
                .argument(IntegerArgument.newBuilder("id"))
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, Components.parse("Remove a marker by id."))
                .permission("squaremarker.remove")
                .handler(this::execute)
        }
    }

    private fun execute(context: CommandContext<CommandSender>) {
        val sender = context.sender

        val id: Int = context.get("id")

        if (MarkerService.markerExist(id)) {
            MarkerService.removeMarker(id)
            Components.sendPrefixed(sender, "<gray>Removed marker with ID <color:#8411FB>$id<gray>.</gray>")
        } else {
            Components.sendPrefixed(sender, "<gray>No marker with ID <color:#8411FB>$id <gray>found.</gray>")
        }

    }

}