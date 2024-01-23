pluginManagement {
    val kotlinPluginVersion = "1.7.21"
    plugins {
        kotlin("jvm") version kotlinPluginVersion
        kotlin("plugin.allopen") version kotlinPluginVersion
        kotlin("plugin.noarg") version kotlinPluginVersion
        kotlin("plugin.spring") version kotlinPluginVersion
        kotlin("kapt") version "1.7.21"
    }
}

rootProject.name = "loadrover-io"
include("api")
include("gatling")
