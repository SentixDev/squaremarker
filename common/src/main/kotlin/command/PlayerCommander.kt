package dev.sentix.squaremarker.command

import xyz.jpenilla.squaremap.api.WorldIdentifier

interface PlayerCommander : Commander {
    val world: WorldIdentifier
    val x: Double
    val y: Double
    val z: Double
}
