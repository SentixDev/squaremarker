plugins {
    id("fabric-loom")
    id("squaremarker.platform")
    id("com.github.johnrengelman.shadow")
}

val minecraftVersion: String by rootProject
val fabricApiVersion: String by rootProject
val fabricLoaderVersion: String by rootProject
val cloudVersion: String by rootProject
val adventureFabricVersion: String by rootProject

val projectImpl: Configuration by configurations.creating
configurations.implementation {
    extendsFrom(projectImpl)
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    projectImpl(project(":squaremarker-common"))

    // We don't include() these since squaremap already does and we depend on it
    modImplementation("cloud.commandframework:cloud-fabric:$cloudVersion")
    modImplementation("net.kyori:adventure-platform-fabric:$adventureFabricVersion")
}

tasks {
    shadowJar {
        configurations = listOf(projectImpl)
        // Don't shade cloud
        dependencies {
            exclude { it.moduleGroup == "cloud.commandframework" }
        }
    }
    remapJar {
        inputFile.set(shadowJar.flatMap { it.archiveFile })
    }
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

squareMarker {
    productionJar.set(tasks.remapJar.flatMap { it.archiveFile })
}
