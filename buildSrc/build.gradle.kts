plugins {
    `kotlin-dsl`
    alias(libs.plugins.ktlint)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.fabricmc.net/")
    maven("https://repo.jpenilla.xyz/snapshots/")
}

dependencies {
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.ktlintGradle)
}
