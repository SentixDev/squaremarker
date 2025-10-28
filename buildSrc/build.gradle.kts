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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:12.0.2")
    implementation("com.gradleup.shadow:com.gradleup.shadow.gradle.plugin:9.2.2")
    implementation("quiet-fabric-loom:quiet-fabric-loom.gradle.plugin:1.11-SNAPSHOT")
}
