import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
    id("org.jlleitschuh.gradle.ktlint")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }

    withType<ShadowJar> {
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
        languageVersion = JavaLanguageVersion.of(21)
    }
}
