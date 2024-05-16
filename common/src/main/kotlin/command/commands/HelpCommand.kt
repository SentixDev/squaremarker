package dev.sentix.squaremarker.command.commands

import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.command.SquaremarkerCommand
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.incendo.cloud.component.CommandComponent
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.minecraft.extras.AudienceProvider
import org.incendo.cloud.minecraft.extras.MinecraftHelp
import org.incendo.cloud.minecraft.extras.RichDescription.richDescription
import org.incendo.cloud.parser.standard.StringParser.greedyStringParser
import org.incendo.cloud.suggestion.Suggestion.suggestion
import org.incendo.cloud.suggestion.SuggestionProvider

class HelpCommand(plugin: SquareMarker, commands: Commands) :
    SquaremarkerCommand(
        plugin,
        commands,
    ) {
    private val help = createHelp()

    override fun register() {
        val helpQueryArgument =
            CommandComponent.builder<Commander, String>("query", greedyStringParser())
                .suggestionProvider(
                    SuggestionProvider.blocking { context, _ ->
                        commands.commandManager.createHelpHandler().queryRootIndex(context.sender())
                            .entries()
                            .map { it.syntax() }
                            .map { suggestion(it) }
                            .toList()
                    },
                )
                .optional()
                .build()

        commands.registerSubcommand { builder ->
            builder.literal("help")
                .argument(helpQueryArgument)
                .commandDescription(richDescription(Components.parse("Show marker help.")))
                .permission("squaremarker.help")
                .handler(::execute)
        }
    }

    private fun execute(context: CommandContext<Commander>) {
        help.queryCommands(context.getOrDefault("query", ""), context.sender())
    }

    private fun createHelp(): MinecraftHelp<Commander> {
        return MinecraftHelp.builder<Commander>()
            .commandManager(commands.commandManager)
            .audienceProvider(AudienceProvider.nativeAudience())
            .commandPrefix("/${squareMarker.config.commandLabel} help")
            .colors(
                MinecraftHelp.helpColors(
                    TextColor.color(0x5B00FF),
                    NamedTextColor.WHITE,
                    TextColor.color(0xC028FF),
                    NamedTextColor.GRAY,
                    NamedTextColor.DARK_GRAY,
                ),
            )
            .messages(MinecraftHelp.MESSAGE_HELP_TITLE, "squaremarker command help")
            .build()
    }
}
