package dev.sentix.squaremarker.command

import dev.sentix.squaremarker.SquareMarker

abstract class SquaremarkerCommand protected constructor(
    private val squareMarker: SquareMarker,
    protected val commands: Commands
) {
    abstract fun register()
}
