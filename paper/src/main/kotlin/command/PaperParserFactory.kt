package dev.sentix.squaremarker.paper.command

import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.ParserFactory
import dev.sentix.squaremarker.command.ParserFactory.XZ
import org.incendo.cloud.bukkit.parser.location.Location2DParser.location2DParser
import org.incendo.cloud.kotlin.extension.mapSuccess
import org.incendo.cloud.paper.parser.KeyedWorldParser.keyedWorldParser
import org.incendo.cloud.parser.ParserDescriptor
import xyz.jpenilla.squaremap.api.MapWorld
import xyz.jpenilla.squaremap.api.WorldIdentifier
import java.util.concurrent.CompletableFuture

object PaperParserFactory : ParserFactory {
    override fun mapWorldParser(): ParserDescriptor<Commander, MapWorld> =
        keyedWorldParser<Commander>().flatMapSuccess(MapWorld::class.java) { _, loc ->
            mapId(WorldIdentifier.parse(loc.key.asString()))
        }

    override fun xzParser(): ParserDescriptor<Commander, XZ> =
        location2DParser<Commander>().mapSuccess { _, pos ->
            CompletableFuture.completedFuture(XZ(pos.blockX, pos.blockZ))
        }
}
