package dev.sentix.squaremarker.command.commands

import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.command.SquaremarkerCommand
import dev.sentix.squaremarker.marker.Marker
import dev.sentix.squaremarker.marker.MarkerService
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.minecraft.extras.RichDescription.richDescription
import org.incendo.cloud.parser.standard.IntegerParser.integerParser

class ShowMarkerCommand(plugin: SquareMarker, commands: Commands) :
    SquaremarkerCommand(
        plugin,
        commands,
    ) {
    override fun register() {
        commands.registerSubcommand { builder ->
            builder.literal("show")
                .required("id", integerParser())
                .commandDescription(richDescription(Components.parse("Show a marker by id.")))
                .permission("squaremarker.show")
                .handler(::execute)
        }
    }

    private fun execute(context: CommandContext<Commander>) {
        val sender = context.sender()

        val id: Int = context.get("id")

        if (MarkerService.markerExist(id)) {
            sendMarkerOverview(sender, MarkerService.getMarker(id))
        } else {
            Components.sendPrefixed(sender, "<gray>No marker with ID <color:#8411FB>$id <gray>found.</gray>")
        }
    }

    private fun sendMarkerOverview(
        sender: Commander,
        marker: Marker,
    ) {
        val gradient = Components.gradient("<b>Marker</b>")
        val div =
            "<dark_gray>» <dark_gray><st>-------------<reset> <gray>× $gradient <gray>× <dark_gray><st>-------------<reset> <dark_gray>«"

        Components.send(sender, "")
        Components.send(
            sender,
            div,
        )
        Components.send(sender, "")

        Components.send(sender, " <gray>× <color:#8411FB>ID <dark_gray>| <color:#8411FB>${marker.id}")
        if (marker.content.isNotBlank()) {
            Components.send(
                sender,
                " <gray>× <color:#8411FB>TEXT <dark_gray>| <color:#8411FB>${marker.content}",
            )
        }
        if (marker.iconUrl.isNotBlank()) {
            Components.send(
                sender,
                " <gray>× <color:#8411FB>URL <dark_gray>| <color:#8411FB>${
                    Components.url(
                        "<color:#8411FB><u>${marker.iconUrl}",
                        "<color:#8411FB>SHOW",
                        marker.iconUrl,
                    )
                }",
            )
        }

        Components.send(sender, "")
        Components.send(
            sender,
            " <gray>× ${
                Components.clickable(
                    "<dark_gray>[<color:#8411FB>UPDATE</color>]",
                    "<color:#8411FB>UPDATE MARKER",
                    "suggest_command",
                    "/squaremarker update ${marker.id} ",
                )
            } ${
                Components.clickable(
                    "<dark_gray>[<color:#8411FB>REMOVE</color>]",
                    "<color:#8411FB>REMOVE MARKER",
                    "/squaremarker remove ${marker.id}",
                )
            }",
        )
        Components.send(sender, "")
        Components.send(
            sender,
            " <gray>× ${
                Components.clickable(
                    "<dark_gray>[<color:#8411FB>LIST</color>]",
                    "<color:#8411FB>SHOW LIST",
                    "/squaremarker list",
                )
            }",
        )
        Components.send(sender, "")
        Components.send(
            sender,
            div,
        )
        Components.send(sender, "")
    }
}
