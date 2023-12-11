plugins {
    `kotlin-dsl`
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    maven("https://repo.jpenilla.xyz/snapshots/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.21")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:12.0.2")
    implementation("com.github.johnrengelman:shadow:8.1.1")
    implementation("xyz.jpenilla:quiet-architectury-loom:1.4-SNAPSHOT")
}
