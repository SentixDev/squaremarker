package dev.sentix.squaremarker

import dev.sentix.squaremarker.command.CommandManager
import dev.sentix.squaremarker.marker.API
import org.bukkit.configuration.Configuration
import org.bukkit.plugin.java.JavaPlugin

class SquareMarker : JavaPlugin() {

    companion object {
        lateinit var main: SquareMarker
        lateinit var configuration: Configuration
    }

    override fun onEnable() {
        main = this

        initConfiguration()

        initCommandManager()

        IO.init()

        API.init()
    }

    override fun onDisable() {
        API.unregister()
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

    private fun initCommandManager() {
        try {
            CommandManager(this)
        } catch (e: Exception) {
            println("Failed to initialize command manager!")
            println(e)
            this.isEnabled = false
            return
        }
    }

}