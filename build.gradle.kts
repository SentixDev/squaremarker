plugins {
    kotlin("jvm") version "1.6.10"
    id("io.papermc.paperweight.userdev") version "1.3.3"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    application
}

group = "dev.sentix"
version = "1.0.0"
description = "Experimental Pl3xMap-Marker recode using Paperweight and the Cloud-Commandframework"

val minecraftVersion: String by project
val cloudVersion: String by project
val bstatsVersion: String by project
val squaremapVersion: String by project
val minimessageVersion: String by project
val adventureVersion: String by project

repositories {
    mavenCentral()
    maven("https://repo.jpenilla.xyz/snapshots/")
}

dependencies {
    implementation(kotlin("stdlib"))

    paperDevBundle("$minecraftVersion-R0.1-SNAPSHOT")

    implementation("cloud.commandframework", "cloud-paper", cloudVersion)
    implementation("cloud.commandframework", "cloud-annotations", cloudVersion)
    implementation("cloud.commandframework", "cloud-minecraft-extras", cloudVersion)
    implementation("cloud.commandframework", "cloud-kotlin-extensions", cloudVersion)

    implementation("org.bstats", "bstats-bukkit", bstatsVersion)

    implementation("net.kyori", "adventure-text-minimessage", minimessageVersion)

    implementation("net.kyori", "adventure-api", adventureVersion)

    compileOnly("xyz.jpenilla", "squaremap-api", squaremapVersion)
}

application {
    mainClass.set("dev.sentix.squaremarker.SquareMarkerKt")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    shadowJar {
        listOf(
            "cloud.commandframework",
            "org.bstats"
        ).forEach { relocate(it, "${rootProject.group}.lib.$it") }
    }
}

bukkit {
    depend = listOf("squaremap")
    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    name = "squaremarker"
    prefix = "squaremarker"
    main = "dev.sentix.squaremarker.SquareMarker"
    apiVersion = "1.18"
    authors = listOf("Sentix")
    website = "https://github.com/SentixDev/squaremarker"
}