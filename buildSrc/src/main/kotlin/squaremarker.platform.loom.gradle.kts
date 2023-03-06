plugins {
    id("squaremarker.platform")
    id("dev.architectury.loom")
    id("com.github.johnrengelman.shadow")
}

val minecraftVersion: String by rootProject

val projectImpl: Configuration by configurations.creating
configurations.implementation {
    extendsFrom(projectImpl)
}

loom.silentMojangMappingsLicense()

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    projectImpl(project(":squaremarker-common"))
}

val markerExt = the<SquareMarkerPlatformExtension>()
markerExt.productionJar.set(tasks.remapJar.flatMap { it.archiveFile })

tasks {
    shadowJar {
        configurations = listOf(projectImpl)
        dependencies {
            exclude {
                it.moduleGroup == "cloud.commandframework"
            }
        }
    }
    remapJar {
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        archiveFileName.set("${project.name}-mc$minecraftVersion-${project.version}.jar")
    }
}

afterEvaluate {
    tasks.processResources {
        inputs.property("version", project.version)

        filesMatching(markerExt.modInfoFilePath.get()) {
            expand("version" to project.version)
        }
    }
}
