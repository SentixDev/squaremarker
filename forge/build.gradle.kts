plugins {
    id("squaremarker.platform.loom")
}

val cloudVersion: String by rootProject
val adventureVersion: String by rootProject

dependencies {
    forge("net.minecraftforge:forge:1.19.3-44.1.0")

    // We don't include() these since squaremap already does and we depend on it
    modImplementation("cloud.commandframework:cloud-forge:1.0.0+1.19.3-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:$adventureVersion")
}

squareMarker {
    modInfoFilePath.set("META-INF/mods.toml")
}
