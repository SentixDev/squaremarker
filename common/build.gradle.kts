val squaremapVersion: String by rootProject
val cloudVersion: String by rootProject
val adventureVersion: String by rootProject
val gsonVersion: String by rootProject
val configurateVersion: String by rootProject

dependencies {
    compileOnlyApi("xyz.jpenilla", "squaremap-api", squaremapVersion)
    api(platform("cloud.commandframework:cloud-bom:$cloudVersion"))
    api("cloud.commandframework", "cloud-core")
    api("cloud.commandframework", "cloud-brigadier")
    api("cloud.commandframework", "cloud-annotations")
    api("cloud.commandframework", "cloud-minecraft-extras") {
        isTransitive = false
    }
    api("cloud.commandframework", "cloud-kotlin-extensions")
    compileOnly("com.google.code.gson", "gson", gsonVersion)
    compileOnly("net.kyori", "adventure-text-minimessage", adventureVersion)
    compileOnly("net.kyori", "adventure-text-logger-slf4j", adventureVersion)
    api(platform("org.spongepowered:configurate-bom:$configurateVersion"))
    api("org.spongepowered:configurate-yaml")
}
