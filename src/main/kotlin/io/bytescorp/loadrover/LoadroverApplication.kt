package io.bytescorp.loadrover

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class LoadroverApplication

fun main(args: Array<String>) {
	runApplication<LoadroverApplication>(*args)
}
