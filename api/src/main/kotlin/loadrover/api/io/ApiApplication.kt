package loadrover.api.io

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["loadrover.api.io", "gatling"])
@EntityScan(basePackages = ["loadrover.api.io", "gatling"])
@ConfigurationPropertiesScan(basePackages = ["loadrover.api.io", "gatling"])
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
