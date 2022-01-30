package dev.sentix.squaremarker.command.commands

import cloud.commandframework.arguments.standard.IntegerArgument
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
import java.io.File

class UpdateMarkerCommand(plugin: SquareMarker, commandManager: CommandManager) :
    SquaremarkerCommand(
        plugin,
        commandManager
    ) {

    override fun register() {
        this.commandManager.registerSubcommand { builder ->
            builder.literal("update")
                .argument(IntegerArgument.newBuilder<CommandSender>("id").build())
                .argument(StringArgument.newBuilder<CommandSender>("input").greedy().asOptionalWithDefault(" "))
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, Components.parse("Update a marker to your position."))
                .permission("squaremarker.set")
                .handler(this::execute)
        }
    }

    private fun execute(context: CommandContext<CommandSender>) {

        val sender = context.sender

        if (sender is Player) {

            val id: Int = context.get("id")

            val iconKey = "squaremap_marker_$id"

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
                content,
                url,
                iconKey,
                sender.location.world.name,
                sender.location.x,
                sender.location.x,
                sender.location.z
            )

            if (MarkerService.markerExist(id)) {

                if (MarkerService.getMarker(id).iconUrl != "") {
                    SquaremapProvider.get().iconRegistry().unregister(Key.key(marker.iconKey))
                    File("${SquaremapProvider.get().webDir()}/images/icon/registered/${marker.iconKey}.png").delete()
                }
                MarkerService.updateMarker(marker)
                Components.sendPrefixed(sender, "<gray>Updated existing marker with ID <color:#8411FB>$id<gray>.</gray>")

            } else {
                Components.sendPrefixed(sender, "<gray>No marker with ID <color:#8411FB>$id <gray>found.</gray>")
            }

        }

    }

}