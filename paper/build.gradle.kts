plugins {
    id("squaremarker.platform")
}

val minecraftVersion: String by rootProject
val bstatsVersion: String by rootProject
val cloudMinecraftModdedVersion: String by rootProject

dependencies {
    implementation(project(":squaremarker-common"))

    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")

    implementation("org.incendo", "cloud-paper", cloudMinecraftModdedVersion)

    implementation("org.bstats", "bstats-bukkit", bstatsVersion)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }

    jar {
        archiveClassifier = "not-shadowed"
    }

    shadowJar {
        archiveClassifier = null as String?
        listOf(
            "cloud.commandframework",
        ).forEach { relocate(it, "${rootProject.group}.lib.$it") }
        dependencies {
            exclude(dependency("org.jetbrains:annotations"))
        }
    }
}

squareMarker {
    productionJar = tasks.shadowJar.flatMap { it.archiveFile }
    modInfoFilePath = "plugin.yml"
}
