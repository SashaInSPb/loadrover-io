pluginManagement {
    val kotlinPluginVersion = "1.7.21"
    plugins {
        kotlin("jvm") version kotlinPluginVersion
        kotlin("plugin.allopen") version kotlinPluginVersion
        kotlin("plugin.noarg") version kotlinPluginVersion
        kotlin("plugin.spring") version kotlinPluginVersion
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "loadrover-io"
include("api")
include("gatling")
include("gatling:kotlin")
findProject(":gatling:kotlin")?.name = "kotlin"
