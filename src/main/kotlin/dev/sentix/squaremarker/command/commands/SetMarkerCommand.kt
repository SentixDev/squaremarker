package dev.sentix.squaremarker.command.commands

import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.context.CommandContext
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys
import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.CommandManager
import dev.sentix.squaremarker.command.SquaremarkerCommand
import dev.sentix.squaremarker.marker.Marker
import dev.sentix.squaremarker.marker.MarkerService
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.jpenilla.squaremap.api.Key
import xyz.jpenilla.squaremap.api.SquaremapProvider
import java.net.URL
import javax.imageio.ImageIO

class SetMarkerCommand(plugin: SquareMarker, commandManager: CommandManager) :
    SquaremarkerCommand(
        plugin,
        commandManager
    ) {

    override fun register() {
        this.commandManager.registerSubcommand { builder ->
            builder.literal("set")
                .argument(StringArgument.newBuilder<CommandSender>("input").greedy().asOptionalWithDefault(" "))
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, Components.parse("Set a marker at your position."))
                .permission("squaremarker.set")
                .handler(this::execute)
        }
    }

    private fun execute(context: CommandContext<CommandSender>) {

        val sender = context.sender

        if (sender is Player) {

            val id: Number = (9..99999).random()

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
                id.toInt(),
                content.trim(),
                url.trim(),
                iconKey,
                sender.location.world.name,
                sender.location.x,
                sender.location.x,
                sender.location.z
            )

            if (!MarkerService.markerExist(id.toInt())) {
                MarkerService.addMarker(marker)
                Components.sendPrefixed(sender, "<gray>Created marker with ID <color:#8411FB>$id<gray>.</gray>")

                try {
                    SquaremapProvider.get().iconRegistry().register(
                        Key.key(marker.iconKey), ImageIO.read(
                            URL(marker.iconUrl)
                        )
                    )
                } catch (ex: Exception,) {
                    Components.sendPrefixed(sender, "<gray>Marker icon set to default.")
                }

            }

        }

    }

}