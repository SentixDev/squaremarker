package dev.sentix.squaremarker.command

import cloud.commandframework.Command
import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.paper.PaperCommandManager
import org.bukkit.command.CommandSender
import dev.sentix.squaremarker.Config
import dev.sentix.squaremarker.SquareMarker
import dev.sentix.squaremarker.command.commands.ListMarkerCommand
import dev.sentix.squaremarker.command.commands.RemoveMarkerCommand
import dev.sentix.squaremarker.command.commands.SetMarkerCommand
import dev.sentix.squaremarker.command.commands.ShowMarkerCommand
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

        mutableListOf(

            ListMarkerCommand(plugin, this),
            RemoveMarkerCommand(plugin, this),
            SetMarkerCommand(plugin, this),
            ShowMarkerCommand(plugin, this)

        ).forEach(SquaremarkerCommand::register)

    }

    fun registerSubcommand(builderModifier: UnaryOperator<Command.Builder<CommandSender>>) {
        this.command(builderModifier.apply(rootBuilder()))
    }

    private fun rootBuilder(): Command.Builder<CommandSender> {
        return this.commandBuilder(
            Config.COMMAND_LABEL,
            "marker", "squaremapmarker", "smarker")
            .meta(CommandMeta.DESCRIPTION, String.format("SquareMarker command. '/%s help'", Config.COMMAND_LABEL))
    }

}