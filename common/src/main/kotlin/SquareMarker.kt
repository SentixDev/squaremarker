package dev.sentix.squaremarker

import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.marker.API
import org.incendo.cloud.CommandManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path

class SquareMarker(
    commandManager: CommandManager<Commander>,
    private val configFile: Path,
    val dataDir: Path,
    val worldIdentifierSerializer: Any = WorldIdentifierSerializer,
) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(SquareMarker::class.java)
        lateinit var instance: SquareMarker
    }

    val markerFile: Path = dataDir.resolve("marker.json")
    val config: Configuration = loadConfiguration()

    init {
        instance = this

        Commands(this, commandManager).registerCommands()
    }

    fun init() {
        IO.init()

        API.init()
    }

    fun shutdown() {
        API.unregister()
    }

    private fun loadConfiguration(): Configuration {
        val loader =
            YamlConfigurationLoader
                .builder()
                .path(configFile)
                .nodeStyle(NodeStyle.BLOCK)
                .build()
        val node = loader.load()
        val configInst = node.get(Configuration::class.java) ?: error("Error reading config")
        val save = loader.createNode()
        save.set(configInst)
        loader.save(save)
        return configInst
    }
}
