package dev.sentix.squaremarker.command.commands

import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.command.SquaremarkerCommand
import dev.sentix.squaremarker.marker.Marker
import dev.sentix.squaremarker.marker.MarkerService
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.description.Description
import org.incendo.cloud.minecraft.extras.RichDescription.richDescription
import org.incendo.cloud.parser.standard.StringParser.greedyStringParser
import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.SquaremapProvider
import xyz.jpenilla.squaremap.api.WorldIdentifier
import java.net.URI
import javax.imageio.ImageIO
import kotlin.random.Random.Default.nextInt

class SetatMarkerCommand(plugin: SquareMarker, commands: Commands) :
    SquaremarkerCommand(
        plugin,
        commands,
    ) {
    override fun register() {
        commands.registerSubcommand { builder ->
            builder.literal("setat", "at")
                .required(
                    "dimension e.g minecraft:overworld> <x> <z> <Marker Name> <[+ optional url for icon]",
                    greedyStringParser(),
                    Description.description("Set a marker in a given dimension, for a given x, z coordinate"),
                )
                .commandDescription(richDescription(Components.parse("Set a marker at at a given (optional dimension,) x and z position.")))
                .permission("squaremarker.set")
                .handler(::execute)
        }
    }

    private fun execute(context: CommandContext<Commander>) {
        val sender = context.sender()

        val id: Int = nextInt(9, 100000)

        val iconKey = "squaremarker_marker_icon_$id"

        val command: String = context.get("dimension e.g minecraft:overworld> <x> <z> <Marker Name> <[+ optional url for icon]")

        val args = command.split(" ", limit = 4)

        when {
            args.size < 3 -> {
                Components.sendPrefixed(sender, "<red>Expected a dimension, x and z coords at a minimum.")
                return
            }
            try {
                SquaremapProvider.get().getWorldIfEnabled(WorldIdentifier.parse(args[0])).isEmpty
            } catch (ex: Exception) {
                true
            } -> {
                val temp = args[0]
                Components.sendPrefixed(sender, "<red>$temp is either not a dimension, or is not registered on Squaremap")
                return
            }
            args[1].toDoubleOrNull() == null ||
                args[2].toDoubleOrNull() == null -> {
                val temp = args[1] + " " + args[2]
                Components.sendPrefixed(sender, "<red>Invalid Coordinates: $temp")
                return
            }
        }

        val dimension: WorldIdentifier = SquaremapProvider.get().getWorldIfEnabled(WorldIdentifier.parse(args[0])).get().identifier()

        val x: Double = args[1].toDouble()

        val z: Double = args[2].toDouble()

        val input: String = if (args.size == 4) args[3] else ""

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
                dimension,
                x,
                0.0,
                z,
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
