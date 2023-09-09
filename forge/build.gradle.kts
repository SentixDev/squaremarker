plugins {
    id("squaremarker.platform.loom")
}

val cloudVersion: String by rootProject
val adventureVersion: String by rootProject
val forgeVersion: String by rootProject
val cloudForgeVersion: String by rootProject

dependencies {
    forge("net.minecraftforge:forge:$forgeVersion")

    // We don't include() these since squaremap already does and we depend on it
    modImplementation("cloud.commandframework:cloud-forge:$cloudForgeVersion")
    compileOnly("net.kyori:adventure-api:$adventureVersion")
}

squareMarker {
    modInfoFilePath.set("META-INF/mods.toml")
}
