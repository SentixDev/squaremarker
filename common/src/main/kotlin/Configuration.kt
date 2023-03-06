package dev.sentix.squaremarker

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
class Configuration {
    val commandLabel = "squaremarker"
    val commandAliases = mutableListOf("marker", "squaremapmarker", "smarker")
    val layerName = "Marker"
    val iconUrl = "https://github.com/SentixDev/squaremarker/raw/master/resources/default_icon.png"
    val iconSize = 16
    val showControls = true
    val defaultHidden = false
    val updateRateMilliseconds = 5L * 1000L // 5 seconds
}
