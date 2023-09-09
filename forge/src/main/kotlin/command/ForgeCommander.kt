package dev.sentix.squaremarker.forge.command

import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.PlayerCommander
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.level.ServerPlayer
import xyz.jpenilla.squaremap.api.WorldIdentifier
import java.lang.reflect.Method

open class ForgeCommander(val sender: CommandSourceStack) : Commander, ForwardingAudience.Single {
    override fun audience(): Audience = commandSourceAudienceMethod(null, sender) as Audience

    class Player(sender: CommandSourceStack, private val player: ServerPlayer) : ForgeCommander(sender), PlayerCommander {
        override val world: WorldIdentifier
            get() = WorldIdentifier.parse(player.level().dimension().location().toString())
        override val x: Double
            get() = player.x
        override val y: Double
            get() = player.y
        override val z: Double
            get() = player.z
    }

    companion object {
        val commandSourceAudienceMethod: Method = Class.forName("xyz.jpenilla.squaremap.forge.ForgeAdventure")
            .getDeclaredMethod("commandSourceAudience", CommandSourceStack::class.java)

        fun create(sender: CommandSourceStack): Commander {
            if (sender.player != null) {
                return Player(sender, sender.playerOrException)
            }
            return ForgeCommander(sender)
        }
    }
}
