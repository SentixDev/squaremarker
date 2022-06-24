import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java-library")

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
        }

        withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
            listOf(
                "kotlin",
                "org.bstats",
                "io.leangen.geantyref",
                "org.spongepowered.configurate",
                "org.yaml.snakeyaml",
            ).forEach { relocate(it, "${rootProject.group}.lib.$it") }
            dependencies {
                exclude(dependency("org.jetbrains:annotations"))
            }
        }
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

tasks.jar {
    enabled = false
}
