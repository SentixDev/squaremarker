package dev.sentix.squaremarker.command.commands

import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.command.PlayerCommander
import dev.sentix.squaremarker.command.SquaremarkerCommand
import dev.sentix.squaremarker.marker.ImageLoader
import dev.sentix.squaremarker.marker.Marker
import dev.sentix.squaremarker.marker.MarkerService
import org.incendo.cloud.component.DefaultValue
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.minecraft.extras.RichDescription.richDescription
import org.incendo.cloud.parser.standard.IntegerParser.integerParser
import org.incendo.cloud.parser.standard.StringParser.greedyStringParser
import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.SquaremapProvider

class UpdateMarkerCommand(
    plugin: SquareMarker,
    commands: Commands,
) : SquaremarkerCommand(
        plugin,
        commands,
    ) {
    override fun register() {
        commands.registerSubcommand { builder ->
            builder
                .literal("update")
                .required("id", integerParser())
                .optional("input", greedyStringParser(), DefaultValue.constant(" "))
                .commandDescription(richDescription(Components.parse("Update a marker to your position.")))
                .permission("squaremarker.set")
                .senderType(PlayerCommander::class.java)
                .handler(::execute)
        }
    }

    private fun execute(context: CommandContext<PlayerCommander>) {
        val sender = context.sender()
        val id: Int = context.get("id")

        if (!MarkerService.markerExist(id)) {
            Components.sendPrefixed(sender, "<gray>No marker with ID <color:#8411FB>$id <gray>found.</gray>")
            return
        }

        val existing = MarkerService.getMarker(id)
        val iconKey = "squaremarker_marker_icon_$id"
        val input: String = context.get("input")
        val (content, iconUrl) = parseInput(input)

        // Unregister the old icon only when the marker previously had one.
        if (existing.iconUrl.isNotBlank()) {
            SquaremapProvider.get().iconRegistry().unregister(Key.of(existing.iconKey))
        }

        // Register the new icon when one was provided.
        if (iconUrl.isNotBlank()) {
            try {
                SquaremapProvider.get().iconRegistry().register(
                    Key.of(iconKey),
                    ImageLoader.load(iconUrl),
                )
            } catch (ex: Exception) {
                Components.sendPrefixed(sender, "<gray>Could not load icon, marker will use the default icon. Error: ${ex.message}")
            }
        }

        val updated = Marker(id, content, iconUrl, iconKey, sender.world, sender.x, sender.y, sender.z)
        MarkerService.updateMarker(updated)
        Components.sendPrefixed(sender, "<gray>Updated existing marker with ID <color:#8411FB>$id<gray>.</gray>")
    }
}
