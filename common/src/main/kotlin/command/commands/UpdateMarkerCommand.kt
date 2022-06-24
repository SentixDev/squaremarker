package dev.sentix.squaremarker.command.commands

import cloud.commandframework.arguments.standard.IntegerArgument
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
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

class UpdateMarkerCommand(plugin: SquareMarker, commands: Commands) :
    SquaremarkerCommand(
        plugin,
        commands
    ) {

    override fun register() {
        this.commands.registerSubcommand { builder ->
            builder.literal("update")
                .argument(IntegerArgument.newBuilder<Commander>("id").build())
                .argument(StringArgument.newBuilder<Commander>("input").greedy().asOptionalWithDefault(" "))
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, Components.parse("Update a marker to your position."))
                .permission("squaremarker.set")
                .handler(this::execute)
        }
    }

    private fun execute(context: CommandContext<Commander>) {

        val sender = context.sender

        if (sender is PlayerCommander) {

            val id: Int = context.get("id")

            val iconKey = "squaremarker_marker_icon_$id"

            val input: String = context.get("input")

            var content = input

            var url = ""

            if (input.contains("http")) {
                val split = input.split("http")

                content = split[0]

                url = "http${split[1]}"
            }

            val marker = Marker(
                id,
                content.trim(),
                url.trim(),
                iconKey,
                sender.world,
                sender.x,
                sender.y,
                sender.z
            )

            if (MarkerService.markerExist(id)) {

                if (MarkerService.getMarker(id).iconUrl.isNotBlank()) {
                    SquaremapProvider.get().iconRegistry().unregister(Key.key(marker.iconKey))
                    File("${SquaremapProvider.get().webDir()}/images/icon/registered/${marker.iconKey}.png").delete()
                }

                try {
                    SquaremapProvider.get().iconRegistry().register(
                        Key.key(marker.iconKey), ImageIO.read(
                            URL(marker.iconUrl)
                        )
                    )
                } catch (ex: Exception) {
                    Components.sendPrefixed(sender, "<gray>Marker icon set to default.")
                }

                MarkerService.updateMarker(marker)
                Components.sendPrefixed(sender, "<gray>Updated existing marker with ID <color:#8411FB>$id<gray>.</gray>")

            } else {
                Components.sendPrefixed(sender, "<gray>No marker with ID <color:#8411FB>$id <gray>found.</gray>")
            }

        }

    }

}