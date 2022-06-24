package dev.sentix.squaremarker

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object Components {

    fun parse(input: String): Component {
        return MiniMessage.miniMessage().deserialize(input)
    }

    fun send(audience: Audience, input: String) {
        audience.sendMessage(parse("<gray>$input"))
    }

    fun sendPrefixed(audience: Audience, input: String) {
        audience.sendMessage(parse("${Lang.PREFIX} <gray>$input"))
    }

    fun url(content: String, hoverText: String, openUrl: String): String {
        return "${hoverable(hoverText)}<click:open_url:$openUrl>$content</hover>"
    }

    fun clickable(content: String, hoverText: String, clickExecution: String): String {
        return "${hoverable(hoverText)}<click:run_command:$clickExecution>$content</hover>"
    }

    /*
        run_command
        suggest_command
        copy_to_clipboard
        open_file
        open_url
     */
    fun clickable(content: String, hoverText: String, clickAction: String, clickExecution: String): String {
        return "${hoverable(hoverText)}<click:$clickAction:$clickExecution>$content</hover>"
    }

    fun hoverable(hoverText: String): String {
        return "<hover:show_text:'$hoverText'>"
    }

    fun gradient(input: String): String {
        return "<gradient:#C028FF:#5B00FF>$input</gradient>"
    }

}