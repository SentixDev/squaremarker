package dev.sentix.squaremarker.command.commands

import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.Lang
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.command.SquaremarkerCommand
import dev.sentix.squaremarker.marker.Marker
import dev.sentix.squaremarker.marker.MarkerService
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.minecraft.extras.RichDescription.richDescription

class ListMarkerCommand(
    plugin: SquareMarker,
    commands: Commands,
) : SquaremarkerCommand(
        plugin,
        commands,
    ) {
    override fun register() {
        commands.registerSubcommand { builder ->
            builder
                .literal("list")
                .commandDescription(richDescription(Components.parse("List all markers.")))
                .permission("squaremarker.list")
                .handler(::execute)
        }
    }

    private fun execute(context: CommandContext<Commander>) {
        val sender = context.sender()

        val markerList = MarkerService.getMarkerList()

        if (markerList.isNotEmpty()) {
            sendMarkerList(sender, markerList)
        } else {
            Components.send(sender, Lang.EMPTY)
        }
    }

    private fun sendMarkerList(
        sender: Commander,
        markerList: MutableList<Marker>,
    ) {
        val gradient = Components.gradient("<b>Marker</b>")

        Components.send(sender, "")
        Components.send(
            sender,
            "<dark_gray>» <dark_gray><st>-------------<reset> <gray>× $gradient <gray>× <dark_gray><st>-------------<reset> <dark_gray>«",
        )
        Components.send(sender, "")

        Components.send(sender, " <gray>× <color:#8411FB>Markers <gray>[<color:#8411FB>" + markerList.size + "<gray>]")
        Components.send(sender, "")
        for (marker in markerList) {
            Components.send(
                sender,
                " <gray>× <color:#8411FB>${marker.id} <color:#8411FB>${
                    Components.clickable(
                        "<dark_gray>[<color:#8411FB>SHOW</color>]",
                        "<color:#8411FB>SHOW",
                        "/squaremarker show ${marker.id}",
                    )
                }",
            )
        }

        Components.send(sender, "")
        Components.send(
            sender,
            "<dark_gray>» <dark_gray><st>-------------<reset> <gray>× $gradient <gray>× <dark_gray><st>-------------<reset> <dark_gray>«",
        )
        Components.send(sender, "")
    }
}
