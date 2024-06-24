package dev.sentix.squaremarker.command

import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.Lang
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.commands.HelpCommand
import dev.sentix.squaremarker.command.commands.ListMarkerCommand
import dev.sentix.squaremarker.command.commands.RemoveMarkerCommand
import dev.sentix.squaremarker.command.commands.SetMarkerCommand
import dev.sentix.squaremarker.command.commands.SetatMarkerCommand
import dev.sentix.squaremarker.command.commands.ShowMarkerCommand
import dev.sentix.squaremarker.command.commands.UpdateMarkerCommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.incendo.cloud.Command
import org.incendo.cloud.CommandManager
import org.incendo.cloud.description.Description.description
import org.incendo.cloud.exception.InvalidCommandSenderException
import org.incendo.cloud.exception.NoPermissionException
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler
import org.incendo.cloud.util.TypeUtils

class Commands(
    private val squareMarker: SquareMarker,
    val commandManager: CommandManager<Commander>,
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
            SetatMarkerCommand(squareMarker, this),
            ShowMarkerCommand(squareMarker, this),
            UpdateMarkerCommand(squareMarker, this),
        ).forEach(SquaremarkerCommand::register)
    }

    private fun registerExceptionHandlers() {
        MinecraftExceptionHandler.createNative<Commander>()
            .defaultArgumentParsingHandler()
            .defaultInvalidSenderHandler()
            .defaultCommandExecutionHandler()
            .handler(InvalidCommandSenderException::class.java) { _, ctx ->
                val requiredTypeDisplayName =
                    when (ctx.exception().requiredSenderTypes().single()) {
                        PlayerCommander::class.java -> "Players"
                        else -> TypeUtils.simpleName(ctx.exception().requiredSenderTypes().single())
                    }
                text()
                    .content("This command can only be executed by ")
                    .color(NamedTextColor.RED)
                    .append(text(requiredTypeDisplayName, NamedTextColor.GRAY))
                    .append(text('!'))
                    .build()
            }
            .handler(NoPermissionException::class.java) { _, _ -> Components.parse(Lang.NO_PERMISSION) }
            .decorator { c -> Components.parse(Lang.HELP).append(c) }
            .registerTo(commandManager)
    }

    fun registerSubcommand(builderModifier: (Command.Builder<Commander>) -> Command.Builder<out Commander>) {
        commandManager.command(builderModifier(rootBuilder()))
    }

    private fun rootBuilder(): Command.Builder<Commander> =
        commandManager.commandBuilder(
            squareMarker.config.commandLabel,
            description("Squaremarker command. '/${squareMarker.config.commandLabel} help'"),
            *squareMarker.config.commandAliases.toTypedArray(),
        )
}
