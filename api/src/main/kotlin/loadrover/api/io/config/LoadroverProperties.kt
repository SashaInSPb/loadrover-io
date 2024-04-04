package loadrover.api.io.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix ="loadrover")
data class LoadroverProperties(
    val downloadDirectory: String,
    val workingDirectory: String,
    val reportDirectory: String
)