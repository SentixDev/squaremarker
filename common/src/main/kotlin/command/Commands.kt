package dev.sentix.squaremarker.command

import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import cloud.commandframework.exceptions.InvalidCommandSenderException
import cloud.commandframework.execution.FilteringCommandSuggestionProcessor
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.minecraft.extras.AudienceProvider
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler
import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.Lang
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.commands.HelpCommand
import dev.sentix.squaremarker.command.commands.ListMarkerCommand
import dev.sentix.squaremarker.command.commands.RemoveMarkerCommand
import dev.sentix.squaremarker.command.commands.SetMarkerCommand
import dev.sentix.squaremarker.command.commands.ShowMarkerCommand
import dev.sentix.squaremarker.command.commands.UpdateMarkerCommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor

class Commands(
    private val squareMarker: SquareMarker,
    val commandManager: CommandManager<Commander>,
) {
    init {
        registerExceptionHandlers()
        commandManager.commandSuggestionProcessor(
            FilteringCommandSuggestionProcessor(
                FilteringCommandSuggestionProcessor.Filter.contains<Commander>(true).andTrimBeforeLastSpace(),
            ),
        )
    }

    fun registerCommands() {
        listOf(
            HelpCommand(squareMarker, this),
            ListMarkerCommand(squareMarker, this),
            RemoveMarkerCommand(squareMarker, this),
            SetMarkerCommand(squareMarker, this),
            ShowMarkerCommand(squareMarker, this),
            UpdateMarkerCommand(squareMarker, this),
        ).forEach(SquaremarkerCommand::register)
    }

    private fun registerExceptionHandlers() {
        MinecraftExceptionHandler<Commander>()
            .withArgumentParsingHandler()
            .withInvalidSyntaxHandler()
            .withCommandExecutionHandler()
            .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SENDER) { _, ex ->
                ex as InvalidCommandSenderException
                val requiredTypeDisplayName =
                    when (ex.requiredSender) {
                        PlayerCommander::class.java -> "Players"
                        else -> ex.requiredSender.simpleName
                    }
                text()
                    .content("This command can only be executed by ")
                    .color(NamedTextColor.RED)
                    .append(text(requiredTypeDisplayName, NamedTextColor.GRAY))
                    .append(text('!'))
                    .build()
            }
            .withHandler(MinecraftExceptionHandler.ExceptionType.NO_PERMISSION) { _, _ -> Components.parse(Lang.NO_PERMISSION) }
            .withDecorator { Components.parse(Lang.HELP).append(it) }
            .apply(commandManager, AudienceProvider.nativeAudience())
    }

    fun registerSubcommand(builderModifier: (Command.Builder<Commander>) -> Command.Builder<Commander>) {
        commandManager.command(builderModifier(rootBuilder()))
    }

    private fun rootBuilder(): Command.Builder<Commander> =
        commandManager
            .commandBuilder(squareMarker.config.commandLabel, *squareMarker.config.commandAliases.toTypedArray())
            .meta(CommandMeta.DESCRIPTION, "Squaremarker command. '/${squareMarker.config.commandLabel} help'")
}
