plugins {
    id("squaremarker.base")
}

val squaremapVersion: String by rootProject
val cloudVersion: String by rootProject
val cloudMinecraftVersion: String by rootProject
val adventureVersion: String by rootProject
val gsonVersion: String by rootProject
val configurateVersion: String by rootProject

dependencies {
    compileOnlyApi("xyz.jpenilla:squaremap-api:$squaremapVersion")
    api(platform("org.incendo:cloud-bom:$cloudVersion"))
    api(platform("org.incendo:cloud-minecraft-bom:$cloudMinecraftVersion"))
    api("org.incendo:cloud-core")
    api("org.incendo:cloud-brigadier")
    api("org.incendo:cloud-annotations")
    api("org.incendo:cloud-minecraft-extras") {
        isTransitive = false
    }
    api("org.incendo:cloud-kotlin-extensions")
    compileOnly("com.google.code.gson:gson:$gsonVersion")
    compileOnly("net.kyori:adventure-text-minimessage:$adventureVersion")
    compileOnly("net.kyori:adventure-text-logger-slf4j:$adventureVersion")
    api(platform("org.spongepowered:configurate-bom:$configurateVersion"))
    api("org.spongepowered:configurate-yaml")
}
