package loadrover.api.io.config

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
        val source: String,
        val work: String,
        val workPath: String,
        val progress: String,
        val complete: String,
        val result: String
    )
}
