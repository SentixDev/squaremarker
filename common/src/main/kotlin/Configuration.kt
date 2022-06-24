package dev.sentix.squaremarker

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
class Configuration {
    val commandLabel = "squaremarker"
    val layerName = "Marker"
    val iconUrl = "https://cdn.upload.systems/uploads/1zRKxN3t.png"
    val iconSize = 16
    val showControls = true
    val defaultHidden = false
    val updateRateMilliseconds = 5L * 1000L // 5 seconds
}
