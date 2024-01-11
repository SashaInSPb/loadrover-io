package io.bytescorp.loadrover.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "loadrover")
data class LoadroverConfig(
    val output: OutputProperty,
    val gatling: GatlingProperty
) {
    data class OutputProperty(
        val classNamePrefix: String
    )

    data class GatlingProperty(
        val path: String,
        val simulation: String
    )
}
