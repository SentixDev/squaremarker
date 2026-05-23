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
import org.incendo.cloud.parser.standard.StringParser.greedyStringParser
import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.SquaremapProvider
import kotlin.random.Random

class SetMarkerCommand(
    plugin: SquareMarker,
    commands: Commands,
) : SquaremarkerCommand(
        plugin,
        commands,
    ) {
    override fun register() {
        commands.registerSubcommand { builder ->
            builder
                .literal("set")
                .optional("input", greedyStringParser(), DefaultValue.constant(" "))
                .commandDescription(richDescription(Components.parse("Set a marker at your position.")))
                .permission("squaremarker.set")
                .senderType(PlayerCommander::class.java)
                .handler(::execute)
        }
    }

    private fun execute(context: CommandContext<PlayerCommander>) {
        val sender = context.sender()

        // Generate a unique ID, retrying on the rare chance of a collision.
        var id: Int
        do {
            id = Random.nextInt(9, 100000)
        } while (MarkerService.markerExist(id))

        val iconKey = "squaremarker_marker_icon_$id"
        val input: String = context.get("input")

        val (content, iconUrl) = parseInput(input)

        val marker = Marker(id, content, iconUrl, iconKey, sender.world, sender.x, sender.y, sender.z)

        MarkerService.addMarker(marker)
        Components.sendPrefixed(sender, "<gray>Created marker with ID <color:#8411FB>$id<gray>.</gray>")

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
    }
}

internal fun parseInput(input: String): Pair<String, String> {
    val trimmed = input.trim()
    if (trimmed.isBlank()) return Pair("", "")

    val lastSpace = trimmed.lastIndexOf(' ')
    val candidate = if (lastSpace > 0) trimmed.substring(lastSpace + 1) else trimmed

    if (looksLikeIconPath(candidate)) {
        val content = if (lastSpace > 0) trimmed.substring(0, lastSpace).trim() else ""
        return Pair(content, candidate)
    }

    return Pair(trimmed, "")
}

private fun looksLikeIconPath(token: String): Boolean =
    token.startsWith("http://") ||
        token.startsWith("https://") ||
        token.startsWith("./") ||
        token.startsWith("../") ||
        token.startsWith("plugins/") ||
        token.endsWith(".png", ignoreCase = true) ||
        token.endsWith(".jpg", ignoreCase = true) ||
        token.endsWith(".jpeg", ignoreCase = true)
