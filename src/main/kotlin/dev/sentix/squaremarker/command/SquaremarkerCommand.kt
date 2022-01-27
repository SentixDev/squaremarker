package dev.sentix.squaremarker.command

import dev.sentix.squaremarker.SquareMarker

abstract class SquaremarkerCommand protected constructor(
    plugin: SquareMarker, commandManager: CommandManager
) {
    private val plugin: SquareMarker
    protected val commandManager: CommandManager

    init {
        this.plugin = plugin
        this.commandManager = commandManager
    }

    abstract fun register()
}