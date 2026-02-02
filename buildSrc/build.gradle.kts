plugins {
    `kotlin-dsl`
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.0")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:14.0.1")
    implementation("com.gradleup.shadow:com.gradleup.shadow.gradle.plugin:9.3.1")
    implementation("quiet-fabric-loom:quiet-fabric-loom.gradle.plugin:1.15-SNAPSHOT")
}
