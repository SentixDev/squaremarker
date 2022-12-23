plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
}

tasks.jar {
    enabled = false
}
