plugins {
    id("squaremarker.platform.loom")
}

val adventureVersion: String by rootProject
val forgeVersion: String by rootProject
val cloudMinecraftModdedVersion: String by rootProject

dependencies {
    neoForge("net.neoforged:neoforge:$forgeVersion")

    // We don't include() these since squaremap already does and we depend on it
    modImplementation("org.incendo:cloud-neoforge:$cloudMinecraftModdedVersion")
    compileOnly("net.kyori:adventure-api:$adventureVersion")
}

squareMarker {
    modInfoFilePath = "META-INF/neoforge.mods.toml"
}
