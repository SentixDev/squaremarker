package dev.sentix.squaremarker

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

class Components {

    fun parse(input: String): Component {
        return MiniMessage.miniMessage().deserialize(input)
    }

    fun send(audience: Audience, input: String) {
        audience.sendMessage(parse("${Lang.PREFIX} <gray>$input"))
    }

    fun url(content: String, hoverText: String, openUrl: String): String {
        return "${hoverable(hoverText)}<click:open_url:$openUrl>$content</hover>"
    }

    fun clickable(content: String, hoverText: String, clickAction: String): String {
        return "${hoverable(hoverText)}<click:run_command:$clickAction>$content</hover>"
    }

    fun hoverable(hoverText: String): String {
        return "<hover:show_text:'$hoverText'>"
    }

}