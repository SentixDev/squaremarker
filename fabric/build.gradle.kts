plugins {
    id("squaremarker.platform")
    id("quiet-fabric-loom")
}

val projectImpl: Configuration by configurations.creating
configurations.implementation {
    extendsFrom(projectImpl)
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    projectImpl(project(":squaremarker-common"))

    modImplementation(libs.fabricLoader)
    modImplementation(libs.fabricApi)

    // We don't include() these since squaremap already does and we depend on it
    modImplementation(libs.cloudFabric)
    modImplementation(libs.adventurePlatformFabric)
}

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
        archiveFileName.set("${project.name}-mc${libs.versions.minecraft.get()}-${project.version}.jar")
    }
}

squareMarker {
    modInfoFilePath = "fabric.mod.json"
    productionJar = tasks.remapJar.flatMap { it.archiveFile }
}
