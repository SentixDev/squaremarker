package dev.sentix.squaremarker.command.commands

import cloud.commandframework.context.CommandContext
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys
import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.command.SquaremarkerCommand

class HelpCommand(plugin: SquareMarker, commands: Commands) :
    SquaremarkerCommand(
        plugin,
        commands
    ) {

    override fun register() {
        this.commands.registerSubcommand { builder ->
            builder.literal("help")
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, Components.parse("Show marker help."))
                .permission("squaremarker.help")
                .handler(this::execute)
        }
    }

    private fun execute(context: CommandContext<Commander>) {
        val sender = context.sender

        sendHelpMessage(sender)

    }

    private fun sendHelpMessage(sender: Commander) {
        val gradient = Components.gradient("<b>Marker</b>")
        val div =
            "<dark_gray>» <dark_gray><st>-------------<reset> <gray>× $gradient <gray>× <dark_gray><st>-------------<reset> <dark_gray>«"

        Components.send(sender, "")
        Components.send(sender, div)
        Components.send(sender, "")

        Components.send(sender, " <gray>× <color:#8411FB>Usage:")
        Components.send(sender, "")
        Components.send(
            sender,
            " <gray>× ${
                Components.clickable(
                    "<color:#8411FB>/squaremarker list",
                    "<color:#8411FB>/squaremarker list",
                    "suggest_command",
                    "/squaremarker list"
                )
            }"
        )
        Components.send(
            sender,
            " <gray>× ${
                Components.clickable(
                    "<color:#8411FB>/squaremarker set <white>[content]</white> <white>[icon-url]</white>",
                    "<color:#8411FB>/squaremarker set",
                    "suggest_command",
                    "/squaremarker set "
                )
            }"
        )
        Components.send(
            sender,
            " <gray>× ${
                Components.clickable(
                    "<color:#8411FB>/squaremarker update <white><id></white> <white>[content]</white> <white>[icon-url]</white>",
                    "<color:#8411FB>/squaremarker update",
                    "suggest_command",
                    "/squaremarker update "
                )
            }"
        )
        Components.send(
            sender,
            " <gray>× ${
                Components.clickable(
                    "<color:#8411FB>/squaremarker remove <white><id></white>",
                    "<color:#8411FB>/squaremarker remove",
                    "suggest_command",
                    "/squaremarker remove "
                )
            }"
        )
        Components.send(
            sender,
            " <gray>× ${
                Components.clickable(
                    "<color:#8411FB>/squaremarker show <white><id></white>",
                    "<color:#8411FB>/squaremarker show",
                    "suggest_command",
                    "/squaremarker show "
                )
            }"
        )

        Components.send(sender, "")
        Components.send(sender, div)
        Components.send(sender, "")

    }
}