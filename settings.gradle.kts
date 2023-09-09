rootProject.name = "squaremarker"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://repo.papermc.io/repository/maven-public/")
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
    id("dev.architectury.loom") version "1.3-SNAPSHOT"
}

include("common")
project(":common").name = "squaremarker-common"

include("fabric")
project(":fabric").name = "squaremarker-fabric"

include("forge")
project(":forge").name = "squaremarker-forge"

include("paper")
project(":paper").name = "squaremarker-paper"
