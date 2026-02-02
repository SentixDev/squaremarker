rootProject.name = "squaremarker"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.architectury.dev/")
        maven("https://repo.jpenilla.xyz/snapshots/")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
            mavenContent { snapshotsOnly() }
        }
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("xyz.jpenilla.quiet-fabric-loom-repositories") version "1.15-SNAPSHOT"
    id("net.neoforged.moddev.repositories") version "2.0.140"
}

include("common")
project(":common").name = "squaremarker-common"

include("fabric")
project(":fabric").name = "squaremarker-fabric"

include("neoforge")
project(":neoforge").name = "squaremarker-neoforge"

include("paper")
project(":paper").name = "squaremarker-paper"
