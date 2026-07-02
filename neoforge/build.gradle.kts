plugins {
    id("squaremarker.platform")
    id("net.neoforged.moddev")
}

neoForge {
    version = libs.versions.neoforge.get()
}

val projectImpl = configurations.create("projectImpl")
configurations.implementation {
    extendsFrom(projectImpl)
}

tasks {
    shadowJar {
        configurations = listOf(projectImpl)
        dependencies {
            exclude {
                it.moduleGroup == "org.incendo"
            }
        }
        archiveFileName.set("${project.name}-mc${libs.versions.minecraft.get()}-${project.version}.jar")
    }
}

dependencies {
    projectImpl(project(":squaremarker-common"))
    // We don't include() these since squaremap already does and we depend on it
    implementation(libs.cloudNeoForge)
    compileOnly(libs.adventureApi)
    implementation(libs.adventurePlatformNeoforge)
}

squareMarker {
    modInfoFilePath = "META-INF/neoforge.mods.toml"
    productionJar = tasks.shadowJar.flatMap { it.archiveFile }
}
