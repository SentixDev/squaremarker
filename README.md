# squaremarker

A simple marker extension for squaremap (https://github.com/jpenilla/squaremap)
<br>
This plugin is an extension and only works in combination with squaremap.
<br>
It offers the possibility to easily create markers yourself, which are then displayed on the map.<br>
This project is still under development and will be improved and expanded in the future.<br>
Feel free to report any bugs you find via [GitHub Issues](https://github.com/SentixDev/squaremarker/issues).

# Download
Downloads are available via [GitHub Releases](https://github.com/SentixDev/squaremarker/releases).

# Usage

## Commands
| Command                                          | Description                              | Permission            |
|--------------------------------------------------|------------------------------------------|-----------------------|
| `/squaremarker help [query]`                     | Query help for squaremarker commands     | `squaremarker.help`   |
| `/squaremarker list`                             | Shows a list with all existing markers   | `squaremarker.list`   |
| `/squaremarker show <id>`                        | Shows details to a specific marker       | `squaremarker.show`   |
| `/squaremarker set [content] [icon-url]`         | Set a marker at your current location    | `squaremarker.set`    |
| `/squaremarker update <id> [content] [icon-url]` | Update a marker to your current location | `squaremarker.update` |
| `/squaremarker remove <id>`                      | Remove a marker with ID                  | `squaremarker.remove` |

## Configuration
```yaml
# Define the main command label
command-label: squaremarker

# Define the main command aliases
command-aliases:
  - marker
  - squaremapmarker
  - smarker

# Define the name of the layer
layer-name: Marker

# Define the link to the marker-icon-image
icon-url: https://cdn.upload.systems/uploads/1zRKxN3t.png

# Define the icon size
icon-size: 16

# Whether people can show/hide the markers on their map
show-controls: true

# Whether the markers are hidden by default
default-hidden: false

# The rate at which markers will update in milliseconds, defaults to 5000ms or 5 seconds
update-rate-milliseconds: 5000
```

## Additional
***Of course, you can also use HTML tags in the content to customize the marker tooltips.***

**Old marker files of the "Pl3xMap-Marker" addon are not compatible with this plugin.** 
