plugins {
    id("com.github.johnrengelman.shadow")
    id("squaremarker.platform")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

val minecraftVersion: String by rootProject
val bstatsVersion: String by rootProject

dependencies {
    implementation(project(":squaremarker-common"))

    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")

    implementation("cloud.commandframework", "cloud-paper")

    implementation("org.bstats", "bstats-bukkit", bstatsVersion)
}

tasks {
    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    jar {
        archiveClassifier.set("not-shadowed")
    }

    shadowJar {
        archiveClassifier.set(null as String?)
        listOf(
            "cloud.commandframework",
        ).forEach { relocate(it, "${rootProject.group}.lib.$it") }
        dependencies {
            exclude(dependency("org.jetbrains:annotations"))
        }
    }
}

squareMarker {
    productionJar.set(tasks.shadowJar.flatMap { it.archiveFile })
}

bukkit {
    depend = listOf("squaremap")
    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    name = "squaremarker"
    prefix = "squaremarker"
    main = "dev.sentix.squaremarker.paper.SquareMarkerPlugin"
    apiVersion = "1.19"
    authors = listOf("Sentix")
    website = "https://github.com/SentixDev/squaremarker"
}
