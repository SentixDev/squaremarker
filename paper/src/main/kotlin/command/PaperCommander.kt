package dev.sentix.squaremarker.paper.command

import dev.sentix.squaremarker.command.Commander
import dev.sentix.squaremarker.command.PlayerCommander
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import org.bukkit.command.CommandSender
import xyz.jpenilla.squaremap.api.BukkitAdapter
import xyz.jpenilla.squaremap.api.WorldIdentifier

open class PaperCommander(
    val sender: CommandSender,
) : Commander,
    ForwardingAudience.Single {
    override fun audience(): Audience = sender

    class Player(
        private val player: org.bukkit.entity.Player,
    ) : PaperCommander(player),
        PlayerCommander {
        override val world: WorldIdentifier
            get() = BukkitAdapter.worldIdentifier(player.world)
        override val x: Double
            get() = player.location.x
        override val y: Double
            get() = player.location.y
        override val z: Double
            get() = player.location.z
    }

    companion object {
        fun create(sender: CommandSender): Commander {
            if (sender is org.bukkit.entity.Player) {
                return Player(sender)
            }
            return PaperCommander(sender)
        }
    }
}
