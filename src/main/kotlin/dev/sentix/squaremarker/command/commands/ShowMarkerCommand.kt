package dev.sentix.squaremarker.command.commands

import cloud.commandframework.arguments.standard.IntegerArgument
import cloud.commandframework.context.CommandContext
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys
import dev.sentix.squaremarker.Lang
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.CommandManager
import dev.sentix.squaremarker.command.SquaremarkerCommand
import org.bukkit.command.CommandSender

class ShowMarkerCommand(plugin: SquareMarker, commandManager: CommandManager) :
    SquaremarkerCommand(
        plugin,
        commandManager
    ) {

    override fun register() {
        this.commandManager.registerSubcommand { builder ->
            builder.literal("show")
                .argument(IntegerArgument.newBuilder("id"))
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, Lang.parse("Show a marker by id."))
                .permission("squaremarker.show")
                .handler(this::execute)
        }
    }

    private fun execute(context: CommandContext<CommandSender>) {
        val sender = context.sender

        val id: Int = context.get("id")

        Lang.send(sender, "ID: $id")
    }
}