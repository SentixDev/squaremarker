plugins {
    `kotlin-dsl`
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.22")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:11.0.0")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
}
