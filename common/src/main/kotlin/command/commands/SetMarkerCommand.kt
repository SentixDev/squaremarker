package dev.sentix.squaremarker.command.commands

import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.command.PlayerCommander
import dev.sentix.squaremarker.command.SquaremarkerCommand
import dev.sentix.squaremarker.marker.Marker
import dev.sentix.squaremarker.marker.MarkerService
import org.incendo.cloud.component.DefaultValue
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.minecraft.extras.RichDescription.richDescription
import org.incendo.cloud.parser.standard.StringParser.greedyStringParser
import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.SquaremapProvider
import java.net.URI
import javax.imageio.ImageIO
import kotlin.random.Random.Default.nextInt

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

        val id: Int = nextInt(9, 100000)

        val iconKey = "squaremarker_marker_icon_$id"

        val input: String = context.get("input")

        var content = input

        var url = ""

        if (input.contains("http")) {
            val split = input.split("http")

            content = split[0]

            url = "http${split[1]}"
        }

        val marker =
            Marker(
                id,
                content.trim(),
                url.trim(),
                iconKey,
                sender.world,
                sender.x,
                sender.y,
                sender.z,
            )

        if (!MarkerService.markerExist(id)) {
            MarkerService.addMarker(marker)
            Components.sendPrefixed(sender, "<gray>Created marker with ID <color:#8411FB>$id<gray>.</gray>")

            try {
                SquaremapProvider.get().iconRegistry().register(
                    Key.key(marker.iconKey),
                    ImageIO.read(
                        URI.create(marker.iconUrl).toURL(),
                    ),
                )
            } catch (ex: Exception) {
                Components.sendPrefixed(sender, "<gray>Marker icon set to default.")
            }
        }
    }
}
