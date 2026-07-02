import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("squaremarker.platform")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":squaremarker-common"))

    compileOnly(libs.paperApi)

    implementation(libs.cloudPaper)

    implementation(libs.bStatsBukkit)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }

    jar {
        archiveClassifier = "not-shadowed"
    }

    shadowJar {
        archiveClassifier = null as String?
        listOf(
            "kotlin",
            "org.bstats",
            "io.leangen.geantyref",
            "org.spongepowered.configurate",
            "org.yaml.snakeyaml",
            "net.kyori.option",
            "org.incendo",
        ).forEach { relocate(it, "${rootProject.group}.lib.$it") }
        dependencies {
            exclude(dependency("org.jetbrains:annotations"))
            exclude {
                it.moduleGroup == "org.checkerframework" ||
                    it.moduleGroup == "com.google.errorprone" ||
                    it.moduleGroup == "org.apiguardian"
            }
        }
    }
}

squareMarker {
    productionJar = tasks.shadowJar.flatMap { it.archiveFile }
    modInfoFilePath = "plugin.yml"
}
