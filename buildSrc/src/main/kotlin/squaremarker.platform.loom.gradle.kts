plugins {
    id("squaremarker.platform")
    id("xyz.jpenilla.quiet-architectury-loom")
    id("com.gradleup.shadow")
}

val minecraftVersion: String by rootProject

val projectImpl: Configuration by configurations.creating
configurations.implementation {
    extendsFrom(projectImpl)
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    projectImpl(project(":squaremarker-common"))
}

val markerExt = extensions.getByType<SquareMarkerPlatformExtension>()
markerExt.productionJar = tasks.remapJar.flatMap { it.archiveFile }

tasks {
    shadowJar {
        configurations = listOf(projectImpl)
        dependencies {
            exclude {
                it.moduleGroup == "org.incendo"
            }
        }
    }
    remapJar {
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        archiveFileName.set("${project.name}-mc$minecraftVersion-${project.version}.jar")
    }
}
