package loadrover.api.io

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication(scanBasePackages = ["loadrover.api.io"])
@EntityScan(basePackages = ["loadrover.api.io"])
@ConfigurationPropertiesScan(basePackages = ["loadrover.api.io"])
@EnableAsync
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
