package dev.sentix.squaremarker.marker

import xyz.jpenilla.squaremap.api.WorldIdentifier

data class Marker(
    val id: Int,
    val content: String,
    val iconUrl: String,
    val iconKey: String,
    val world: WorldIdentifier,
    val posX: Double,
    val posY: Double,
    val posZ: Double
)
