package dev.sentix.squaremarker

object Config {

    val MARKER_FILE = "${SquareMarker.main.dataFolder.absolutePath}/marker.json"

    val COMMAND_LABEL = SquareMarker.configuration.getString("command-label")!!

    val LAYER_NAME = SquareMarker.configuration.getString("layer-name")!!

    val ICON_URL = SquareMarker.configuration.getString("icon-url")!!

    val ICON_SIZE = SquareMarker.configuration.getInt("icon-size")

    val SHOW_CONTROLS = SquareMarker.configuration.getBoolean("show-controls")

    val DEFAULT_HIDDEN = SquareMarker.configuration.getBoolean("default-hidden")

}