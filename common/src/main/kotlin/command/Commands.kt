package dev.sentix.squaremarker.command

import cloud.commandframework.Command
import cloud.commandframework.CommandManager
import cloud.commandframework.exceptions.NoPermissionException
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

class Commands(
    private val squareMarker: SquareMarker,
    private val commandManager: CommandManager<Commander>
) {
    init {
        registerExceptionHandlers()
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
            .withInvalidSenderHandler()
            .withInvalidSyntaxHandler()
            .withCommandExecutionHandler()
            .withDecorator { Components.parse(Lang.HELP).append(it) }
            .apply(commandManager, AudienceProvider.nativeAudience())

        commandManager.registerExceptionHandler(NoPermissionException::class.java) { sender, _ ->
            Components.send(sender, Lang.NO_PERMISSION)
        }
    }

    fun registerSubcommand(builderModifier: (Command.Builder<Commander>) -> Command.Builder<Commander>) {
        commandManager.command(builderModifier(rootBuilder()))
    }

    private fun rootBuilder(): Command.Builder<Commander> {
        return commandManager.commandBuilder(
            squareMarker.config.commandLabel,
            "marker", "squaremapmarker", "smarker"
        ).meta(CommandMeta.DESCRIPTION, "Squaremarker command. '/${squareMarker.config.commandLabel} help'")
    }
}
