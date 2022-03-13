# squaremarker
A simple marker extension for squaremap (https://github.com/jpenilla/squaremap)
<br>
This plugin is an extension and only works in combination with Pl3xMap.
<br>
It offers the possibility to easily create markers yourself, which are then displayed on the map.<br>
This project is still under development and will be improved and expanded in the future.<br>
Feel free to report any bugs you find via [github issues](https://github.com/SentixDev/squaremarker/issues).

# Download
Downloads are available via [github releases](https://github.com/SentixDev/squaremarker/releases).

# Usage
## Commands:
- ### COMMAND | DESCRIPTION | PERMISSION
- /squaremarker help | Shows a list of existing commands | squaremarker.help
- /squaremarker list | Shows a list with all existing markers | squaremarker.list
- /squaremarker show | Shows details to a specific marker | squaremarker.show
- /squaremarker set | Set a marker at your current location | squaremarker.set
- /squaremarker update | Update a marker at to current location | squaremarker.update
- /squaremarker remove | Remove a marker with ID | squaremarker.remove

## Configuration:
- "command-label": (define the main command label)
- "layer-name:" (define the name of the layer)
- "icon-url:" (define the link to the marker-icon-image)
- "icon-size": (define the icon size)
- "show-controls": (whether people can show/hide the markers on their map)
- "default-hidden": (whether the markers are hidden by default)

## Additional:
***Of course you can also use HTML tags in the content to customize the marker tooltips.***

**Old marker files of the "Pl3xMap-Marker" addon are not compatible with this plugin.** 
