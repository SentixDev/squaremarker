plugins {
    id("squaremarker.base")
}

val platform = extensions.create("squareMarker", SquareMarkerPlatformExtension::class)

tasks {
    val copyJar = register<CopyFile>("copyJar") {
        fileToCopy.set(platform.productionJar)
        destination.set(
            platform.productionJar.flatMap {
                rootProject.layout.buildDirectory.file("libs/${it.asFile.name}")
            }
        )
    }
    assemble {
        dependsOn(copyJar)
    }
}

afterEvaluate {
    tasks.processResources {
        val version = project.version
        inputs.property("version", version)

        filesMatching(platform.modInfoFilePath.get()) {
            expand("version" to version)
        }
    }
}
