plugins {
    id("squaremarker.platform")
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
        toolchain.languageVersion = JavaLanguageVersion.of(17)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 17
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
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
