package dev.sentix.squaremarker.command

import org.incendo.cloud.parser.ArgumentParseResult
import org.incendo.cloud.parser.ParserDescriptor
import xyz.jpenilla.squaremap.api.MapWorld
import xyz.jpenilla.squaremap.api.SquaremapProvider
import xyz.jpenilla.squaremap.api.WorldIdentifier
import java.util.concurrent.CompletableFuture

interface ParserFactory {
    fun mapWorldParser(): ParserDescriptor<Commander, MapWorld>

    data class XZ(val x: Int, val z: Int)

    fun xzParser(): ParserDescriptor<Commander, XZ>

    fun mapId(id: WorldIdentifier): CompletableFuture<ArgumentParseResult<MapWorld>> {
        val mapWorldOptional = SquaremapProvider.get().getWorldIfEnabled(id)

        return if (mapWorldOptional.isEmpty) {
            ArgumentParseResult.failureFuture(RuntimeException("Could not find enabled map world '${id.asString()}'"))
        } else {
            ArgumentParseResult.successFuture(mapWorldOptional.get())
        }
    }
}
