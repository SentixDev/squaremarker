package dev.sentix.squaremarker.paper

val folia: Boolean by lazy {
    val regionizedServerCls: Class<*>? = runCatching {
        Class.forName("io.papermc.paper.threadedregions.RegionizedServer")
    }.getOrNull()
    regionizedServerCls != null
}
