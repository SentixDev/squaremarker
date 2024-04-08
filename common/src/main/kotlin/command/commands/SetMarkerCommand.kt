package dev.sentix.squaremarker.command.commands

import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.context.CommandContext
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys
import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.command.PlayerCommander
import dev.sentix.squaremarker.command.SquaremarkerCommand
import dev.sentix.squaremarker.marker.Marker
import dev.sentix.squaremarker.marker.MarkerService
import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.SquaremapProvider
import java.net.URL
import javax.imageio.ImageIO
import kotlin.random.Random.Default.nextInt

class SetMarkerCommand(plugin: SquareMarker, commands: Commands) :
    SquaremarkerCommand(
        plugin,
        commands,
    ) {
    override fun register() {
        commands.registerSubcommand { builder ->
            builder.literal("set")
                .argument(StringArgument.builder<Commander>("input").greedy().asOptionalWithDefault(" "))
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, Components.parse("Set a marker at your position."))
                .permission("squaremarker.set")
                .senderType(PlayerCommander::class.java)
                .handler(::execute)
        }
    }

    private fun execute(context: CommandContext<Commander>) {
        val sender = context.sender as PlayerCommander

        var id: Int

        val input: String = context.get("input")

        var content = input

        var url = ""

        do {
            id = nextInt(9, 100000)
        } while (MarkerService.markerExist(id))

        val iconKey: String = "squaremarker_marker_icon_$id"

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
                        URL(marker.iconUrl),
                    ),
                )
            } catch (ex: Exception) {
                Components.sendPrefixed(sender, "<gray>Marker icon set to default.")
            }
        }
    }
}
