package dev.sentix.squaremarker.command.commands

import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.command.SquaremarkerCommand
import dev.sentix.squaremarker.marker.MarkerService
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.minecraft.extras.RichDescription.richDescription
import org.incendo.cloud.parser.standard.IntegerParser.integerParser

class RemoveMarkerCommand(
    plugin: SquareMarker,
    commands: Commands,
) : SquaremarkerCommand(
        plugin,
        commands,
    ) {
    override fun register() {
        commands.registerSubcommand { builder ->
            builder
                .literal("remove")
                .required("id", integerParser())
                .commandDescription(richDescription(Components.parse("Remove a marker by id.")))
                .permission("squaremarker.remove")
                .handler(::execute)
        }
    }

    private fun execute(context: CommandContext<Commander>) {
        val sender = context.sender()

        val id: Int = context.get("id")

        if (MarkerService.markerExist(id)) {
            MarkerService.removeMarker(id)
            Components.sendPrefixed(sender, "<gray>Removed marker with ID <color:#8411FB>$id<gray>.</gray>")
        } else {
            Components.sendPrefixed(sender, "<gray>No marker with ID <color:#8411FB>$id <gray>found.</gray>")
        }
    }
}
