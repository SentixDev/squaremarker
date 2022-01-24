package dev.sentix.squaremarker

object Config {

    val MARKER_FILE = "${SquareMarker.main.dataFolder.absolutePath}/marker.json"

    val COMMAND_LABEL = SquareMarker.configuration.getString("command-label")

    val LAYER_NAME = SquareMarker.configuration.getString("layer-name")

    val ICON_URL = SquareMarker.configuration.getString("icon-url")

    val ICON_SIZE = SquareMarker.configuration.getString("icon-size")

}