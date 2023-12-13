plugins {
    id("squaremarker.platform.loom")
}

val cloudVersion: String by rootProject
val adventureVersion: String by rootProject
val forgeVersion: String by rootProject
val cloudForgeVersion: String by rootProject

dependencies {
    neoForge("net.neoforged:neoforge:$forgeVersion")

    // We don't include() these since squaremap already does and we depend on it
    modImplementation("cloud.commandframework:cloud-neoforge:$cloudForgeVersion")
    compileOnly("net.kyori:adventure-api:$adventureVersion")
}

squareMarker {
    modInfoFilePath = "META-INF/mods.toml"
}
