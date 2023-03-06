plugins {
    id("squaremarker.base")
    id("com.github.johnrengelman.shadow")
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
    shadowJar {
        dependencies {
            exclude {
                it.moduleGroup == "org.checkerframework"
                        || it.moduleGroup == "com.google.errorprone"
                        || it.moduleGroup == "org.apiguardian"
            }
        }
    }
    assemble {
        dependsOn(copyJar)
    }
}
