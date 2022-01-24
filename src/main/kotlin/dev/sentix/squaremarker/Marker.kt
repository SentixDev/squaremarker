package dev.sentix.squaremarker

data class Marker(
    val id: Int,
    val content: String,
    val iconUrl: String,
    val iconKey: String,
    val world: String,
    val posX: Double,
    val posY: Double,
    val posZ: Double
)
