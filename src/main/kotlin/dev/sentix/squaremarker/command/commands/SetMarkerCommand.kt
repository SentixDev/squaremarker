package dev.sentix.squaremarker.command.commands

import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.context.CommandContext
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys
import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.Lang
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.CommandManager
import dev.sentix.squaremarker.command.SquaremarkerCommand
import org.bukkit.command.CommandSender

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

        val id: Number = (999..99999).random()

        val input: String = context.get("input")

        var content = input

        var url = ""

        if (input.contains("http")) {
            val split = input.split("http")

            content = split[0]

            url = "http${split[1]}"
        }

        Components.send(sender, "ID: $id")
        Components.send(sender, "CONTENT: $content")
        Components.send(sender, "URL: $url")

    }

}