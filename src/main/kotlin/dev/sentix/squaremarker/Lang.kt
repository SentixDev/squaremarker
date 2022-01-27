package dev.sentix.squaremarker

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage


object Lang {

    const val PREFIX = "<gray>[<gradient:#C028FF:#5B00FF>SquareMarker</gradient>]</gray>"

    const val NO_PERMISSION = "$PREFIX <red>Not a player.</red>"

    const val EMPTY = "$PREFIX <red>No markers set.</red>"

    fun parse(input: String): Component {
        return MiniMessage.miniMessage().deserialize(input)
    }

    fun send(audience: Audience, input: String) {
        audience.sendMessage(parse("$PREFIX <gray>$input"))
    }

}
