plugins {
    base
}

val platform = extensions.create("squareMarker", SquareMarkerPlatformExtension::class)

tasks {
    val copyJar = register<CopyFile>("copyJar") {
        fileToCopy.set(platform.productionJar)
        destination.set(platform.productionJar.flatMap {
            rootProject.layout.buildDirectory.file("libs/${it.asFile.name}")
        })
    }
    assemble {
        dependsOn(copyJar)
    }
}
