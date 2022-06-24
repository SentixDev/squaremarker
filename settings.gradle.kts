rootProject.name = "squaremarker"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
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
}

plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
}

include("common")
project(":common").name = "squaremarker-common"

include("fabric")
project(":fabric").name = "squaremarker-fabric"

include("paper")
project(":paper").name = "squaremarker-paper"
