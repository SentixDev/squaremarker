package dev.sentix.squaremarker.command

import cloud.commandframework.brigadier.BrigadierManagerHolder

object BrigadierSetup {
    fun setup(mgr: BrigadierManagerHolder<*>) {
        val brigManager = mgr.brigadierManager()
            ?: error("CloudBrigadierManager not present?")

        brigManager.setNativeNumberSuggestions(false)
    }
}
