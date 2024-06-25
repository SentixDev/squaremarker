plugins {
    id("squaremarker.platform.loom")
}

val fabricApiVersion: String by rootProject
val fabricLoaderVersion: String by rootProject
val cloudMinecraftModdedVersion: String by rootProject
val adventureFabricVersion: String by rootProject

dependencies {
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    // We don't include() these since squaremap already does and we depend on it
    modImplementation("org.incendo:cloud-fabric:$cloudMinecraftModdedVersion")
    modImplementation("net.kyori:adventure-platform-fabric:$adventureFabricVersion")
}

squareMarker {
    modInfoFilePath = "fabric.mod.json"
}
