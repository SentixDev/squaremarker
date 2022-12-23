rootProject.name = "squaremarker"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.quiltmc.org/repository/release/")
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
    id("quiet-fabric-loom") version "1.0-SNAPSHOT"
}

include("common")
project(":common").name = "squaremarker-common"

include("fabric")
project(":fabric").name = "squaremarker-fabric"

include("paper")
project(":paper").name = "squaremarker-paper"
