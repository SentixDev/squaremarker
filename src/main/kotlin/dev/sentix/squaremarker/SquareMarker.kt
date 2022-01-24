package dev.sentix.squaremarker

import org.bukkit.configuration.Configuration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class SquareMarker : JavaPlugin() {

    companion object {
        lateinit var main: SquareMarker
        lateinit var configuration: Configuration
    }

    override fun onEnable() {
        main = this

        initConfiguration()

        IO.init()
    }

    override fun onDisable() {

    }

    private fun initConfiguration() {
        config.addDefault("command-label", "squaremarker")
        config.addDefault("layer-name", "Marker")
        config.addDefault("icon-url", "https://cdn.upload.systems/uploads/1zRKxN3t.png")
        config.addDefault("icon-size", 16)

        config.options().copyDefaults(true)
        saveConfig()

        configuration = config
    }

}