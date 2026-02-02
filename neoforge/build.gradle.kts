plugins {
    id("squaremarker.platform")
    id("net.neoforged.moddev")
}

val adventureVersion: String by rootProject
val forgeVersion: String by rootProject
val cloudMinecraftModdedVersion: String by rootProject
val adventureFabricVersion: String by rootProject
val minecraftVersion: String by rootProject

neoForge {
    version = forgeVersion
}

val projectImpl: Configuration by configurations.creating
configurations.implementation {
    extendsFrom(projectImpl)
}

tasks {
    shadowJar {
        configurations = listOf(projectImpl)
        dependencies {
            exclude {
                it.moduleGroup == "org.incendo"
            }
        }
        archiveFileName.set("${project.name}-mc$minecraftVersion-${project.version}.jar")
    }
}

dependencies {
    projectImpl(project(":squaremarker-common"))
    // We don't include() these since squaremap already does and we depend on it
    implementation("org.incendo:cloud-neoforge:$cloudMinecraftModdedVersion")
    compileOnly("net.kyori:adventure-api:$adventureVersion")
    implementation("net.kyori:adventure-platform-neoforge:$adventureFabricVersion")
}

squareMarker {
    modInfoFilePath = "META-INF/neoforge.mods.toml"
    productionJar = tasks.shadowJar.flatMap { it.archiveFile }
}
