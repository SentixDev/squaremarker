import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("squaremarker.platform")
    id("xyz.jpenilla.quiet-fabric-loom")
}

val common = configurations.create("common")

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
    }
}

dependencies {
    minecraft(libs.minecraft)
    implementation(project(":squaremarker-common"))
    common(project(":squaremarker-common")) {
        isTransitive = false
    }

    implementation(libs.fabricLoader)
    implementation(libs.fabricApi)

    // We don't include() these since squaremap already does and we depend on it
    implementation(libs.cloudFabric)
    implementation(libs.adventurePlatformFabric)

    implementation(libs.bundles.moddedRuntime)
    include(libs.bundles.moddedRuntime)
}

tasks {
    jar {
        archiveFileName.set("${project.name}-mc${libs.versions.minecraft.get()}-${project.version}.jar")
        from(common.elements.map { files -> files.map { zipTree(it) } }) {
            exclude("META-INF/MANIFEST.MF")
        }
    }
}

squareMarker {
    modInfoFilePath = "fabric.mod.json"
    productionJar = tasks.jar.flatMap { it.archiveFile }
}
