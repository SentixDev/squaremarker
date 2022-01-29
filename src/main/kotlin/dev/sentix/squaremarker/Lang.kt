package dev.sentix.squaremarker

object Lang {

    const val PREFIX = "<gray>[<gradient:#C028FF:#5B00FF>squaremarker</gradient>]</gray>"

    const val NO_PERMISSION = "$PREFIX <red>Not authorized.</red>"

    const val EMPTY = "$PREFIX <red>No markers set.</red>"

    val HELP = Components.clickable("$PREFIX ", Components.gradient("SHOW USAGE"), "/squaremarker help")

}
