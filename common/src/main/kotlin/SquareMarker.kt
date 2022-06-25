package dev.sentix.squaremarker

import cloud.commandframework.CommandManager
import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.Commands
import dev.sentix.squaremarker.marker.API
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path

class SquareMarker(
    commandManager: CommandManager<Commander>,
    private val configFile: Path,
    dataDir: Path,
    val worldIdentifierSerializer: Any = WorldIdentifierSerializer
) {
    companion object {
        val logger: ComponentLogger = ComponentLogger.logger(SquareMarker::class.java)
        lateinit var instance: SquareMarker
    }

    val markerFile: Path = dataDir.resolve("marker.json")
    val config: Configuration = loadConfiguration()

    init {
        instance = this

        Commands(this, commandManager).registerCommands()

        IO.init()

        API.init()
    }

    fun shutdown() {
        API.unregister()
    }

    private fun loadConfiguration(): Configuration {
        val loader = YamlConfigurationLoader.builder()
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
