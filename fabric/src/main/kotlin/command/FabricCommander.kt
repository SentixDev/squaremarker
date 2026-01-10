package dev.sentix.squaremarker.fabric.command

import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.PlayerCommander
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.level.ServerPlayer
import xyz.jpenilla.squaremap.api.WorldIdentifier

open class FabricCommander(val sender: CommandSourceStack) : Commander, ForwardingAudience.Single {
    override fun audience(): Audience = sender

    class Player(sender: CommandSourceStack, private val player: ServerPlayer) : FabricCommander(sender), PlayerCommander {
        override val world: WorldIdentifier
            get() = WorldIdentifier.parse(player.level().dimension().identifier().toString())
        override val x: Double
            get() = player.x
        override val y: Double
            get() = player.y
        override val z: Double
            get() = player.z
    }

    companion object {
        fun create(sender: CommandSourceStack): Commander {
            if (sender.player != null) {
                return Player(sender, sender.playerOrException)
            }
            return FabricCommander(sender)
        }
    }
}
