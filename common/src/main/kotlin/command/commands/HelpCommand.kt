package dev.sentix.squaremarker.command.commands

import cloud.commandframework.CommandHelpHandler
import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.context.CommandContext
import cloud.commandframework.minecraft.extras.AudienceProvider
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys
import cloud.commandframework.minecraft.extras.MinecraftHelp
import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.command.SquaremarkerCommand
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

class HelpCommand(plugin: SquareMarker, commands: Commands) :
    SquaremarkerCommand(
        plugin,
        commands
    ) {

    private val help = createHelp()

    override fun register() {
        val helpQueryArgument = StringArgument.builder<Commander>("query")
            .greedy()
            .asOptional()
            .withSuggestionsProvider { context, _ ->
                val indexHelpTopic = commands.commandManager.createCommandHelpHandler()
                    .queryHelp(context.sender, "") as CommandHelpHandler.IndexHelpTopic<Commander>
                indexHelpTopic.entries.map { it.syntaxString }.toList()
            }
            .build()

        commands.registerSubcommand { builder ->
            builder.literal("help")
                .argument(helpQueryArgument)
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, Components.parse("Show marker help."))
                .permission("squaremarker.help")
                .handler(::execute)
        }
    }

    private fun execute(context: CommandContext<Commander>) {
        help.queryCommands(context.getOrDefault("query", "")!!, context.sender)
    }

    private fun createHelp(): MinecraftHelp<Commander> {
        val help = MinecraftHelp(
            "/${squareMarker.config.commandLabel} help",
            AudienceProvider.nativeAudience(),
            commands.commandManager
        )
        help.helpColors = MinecraftHelp.HelpColors.of(
            TextColor.color(0x5B00FF),
            NamedTextColor.WHITE,
            TextColor.color(0xC028FF),
            NamedTextColor.GRAY,
            NamedTextColor.DARK_GRAY
        )
        help.setMessage(MinecraftHelp.MESSAGE_HELP_TITLE, "squaremarker command help")
        return help
    }
}