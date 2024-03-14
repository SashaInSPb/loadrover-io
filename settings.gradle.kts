pluginManagement {
    val kotlinPluginVersion = "1.7.21"
    plugins {
        // 모듈 별 공통 플러그인을 여기서 관리
        kotlin("jvm") version kotlinPluginVersion
        kotlin("plugin.allopen") version kotlinPluginVersion
        kotlin("plugin.noarg") version kotlinPluginVersion
        kotlin("plugin.spring") version kotlinPluginVersion

        kotlin("plugin.jpa") version kotlinPluginVersion
        kotlin("plugin.spring") version kotlinPluginVersion
    }
}

rootProject.name = "loadrover-io"
include("api")
