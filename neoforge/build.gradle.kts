import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("squaremarker.platform")
    id("net.neoforged.moddev")
}

neoForge {
    version = libs.versions.neoforge.get()
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
    }
}

val common = configurations.create("common")

tasks {
    jar {
        archiveFileName.set("${project.name}-mc${libs.versions.minecraft.get()}-${project.version}.jar")
        from(common.elements.map { files -> files.map { zipTree(it) } }) {
            exclude("META-INF/MANIFEST.MF")
        }
    }
}

dependencies {
    implementation(project(":squaremarker-common"))
    common(project(":squaremarker-common")) {
        isTransitive = false
    }
    // We don't include() these since squaremap already does and we depend on it
    implementation(libs.cloudNeoForge)
    compileOnly(libs.adventureApi)
    implementation(libs.adventurePlatformNeoforge)

    implementation(libs.bundles.moddedRuntime)
    jarJar(libs.bundles.moddedRuntime)
}

squareMarker {
    modInfoFilePath = "META-INF/neoforge.mods.toml"
    productionJar = tasks.jar.flatMap { it.archiveFile }
}
