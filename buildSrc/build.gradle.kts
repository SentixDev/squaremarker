plugins {
    `kotlin-dsl`
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:11.0.0")
    implementation("com.github.johnrengelman:shadow:8.1.0")
    implementation("dev.architectury:architectury-loom:1.1-SNAPSHOT")
}
