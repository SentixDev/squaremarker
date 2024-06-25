package dev.sentix.squaremarker.fabric.command

import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.ParserFactory
import dev.sentix.squaremarker.command.ParserFactory.XZ
import net.minecraft.commands.arguments.DimensionArgument
import net.minecraft.resources.ResourceLocation
import org.incendo.cloud.brigadier.parser.WrappedBrigadierParser
import org.incendo.cloud.kotlin.extension.mapSuccess
import org.incendo.cloud.minecraft.modded.parser.VanillaArgumentParsers
import org.incendo.cloud.parser.ParserDescriptor
import xyz.jpenilla.squaremap.api.MapWorld
import xyz.jpenilla.squaremap.api.WorldIdentifier
import java.util.concurrent.CompletableFuture

object FabricParserFactory : ParserFactory {
    override fun mapWorldParser(): ParserDescriptor<Commander, MapWorld> =
        ParserDescriptor.of(
            WrappedBrigadierParser<Commander, ResourceLocation>(DimensionArgument()).flatMapSuccess { _, loc ->
                mapId(WorldIdentifier.parse(loc.toString()))
            },
            MapWorld::class.java,
        )

    override fun xzParser(): ParserDescriptor<Commander, XZ> =
        VanillaArgumentParsers.columnPosParser<Commander>().mapSuccess { _, columnCoordinates ->
            val pos = columnCoordinates.blockPos()
            CompletableFuture.completedFuture(XZ(pos.x, pos.z))
        }
}
