plugins {
    id("squaremarker.base")
}

dependencies {
    compileOnlyApi(libs.squaremapApi)
    api(platform(libs.cloudBom))
    api(platform(libs.cloudMinecraftBom))
    api(libs.cloudCore)
    api(libs.cloudBrigadier)
    api(libs.cloudAnnotations)
    api(libs.cloudMinecraftExtras) {
        isTransitive = false
    }
    api(libs.cloudKotlinExtensions)
    compileOnly(libs.gson)
    compileOnly(libs.adventureTextMinimessage)
    compileOnly(libs.adventureTextLoggerSlf4j)
    api(platform(libs.configurateBom))
    api(libs.configurateYaml)
}
