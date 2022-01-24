package dev.sentix.squaremarker.command

import dev.sentix.squaremarker.SquareMarker

abstract class SquaremarkerCommand protected constructor(
    private var plugin: SquareMarker, private var commandManager: CommandManager
) {
    abstract fun register()
}