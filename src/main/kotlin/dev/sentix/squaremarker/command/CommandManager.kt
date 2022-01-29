package dev.sentix.squaremarker.command

import cloud.commandframework.Command
import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.exceptions.InvalidSyntaxException
import cloud.commandframework.exceptions.NoPermissionException
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.minecraft.extras.AudienceProvider
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler.ExceptionType
import cloud.commandframework.paper.PaperCommandManager
import dev.sentix.squaremarker.Components
import dev.sentix.squaremarker.Config
import dev.sentix.squaremarker.Lang
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.commands.*
import net.kyori.adventure.identity.Identity
import org.bukkit.command.CommandSender
import java.util.*
import java.util.function.BiConsumer
import java.util.function.UnaryOperator


class CommandManager(
    plugin: SquareMarker
) : PaperCommandManager<CommandSender>(
    plugin, CommandExecutionCoordinator.simpleCoordinator(), UnaryOperator.identity(),
    UnaryOperator.identity()
) {

    init {
        if (this.queryCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            this.registerBrigadier()
            brigadierManager()?.setNativeNumberSuggestions(false)
        }

        if (this.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            this.registerAsynchronousCompletions();
        }

        registerExceptionHandlers()

        mutableListOf(

            HelpCommand(plugin, this),
            ListMarkerCommand(plugin, this),
            RemoveMarkerCommand(plugin, this),
            SetMarkerCommand(plugin, this),
            ShowMarkerCommand(plugin, this)

        ).forEach(SquaremarkerCommand::register)

    }

    private fun registerExceptionHandlers() {

        MinecraftExceptionHandler<CommandSender>()
            .withArgumentParsingHandler()
            .withInvalidSenderHandler()
            .withInvalidSyntaxHandler()
            .withCommandExecutionHandler()
            .withDecorator { Components.parse(Lang.HELP).append(it) }
            .apply(this, AudienceProvider.nativeAudience())

        this.registerExceptionHandler(NoPermissionException::class.java) { sender, _ ->
            Components.send(sender, Lang.NO_PERMISSION)
        }
    }

    fun registerSubcommand(builderModifier: UnaryOperator<Command.Builder<CommandSender>>) {
        this.command(builderModifier.apply(rootBuilder()))
    }

    private fun rootBuilder(): Command.Builder<CommandSender> {
        return this.commandBuilder(
            Config.COMMAND_LABEL,
            "marker", "squaremapmarker", "smarker"
        ).meta(CommandMeta.DESCRIPTION, "Squaremarker command. '/${Config.COMMAND_LABEL} help'")
    }

}