package loadrover.api.io.domain.run

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/run")
@Tag(name = "Run controller", description = "/run")
class RunController {
    private val logger = LoggerFactory.getLogger(RunController::class.java)

    @Async
    @GetMapping()
    @Operation(summary = "부하테스트 실행")
    fun runSimulation() {
        try {
            val processBuilder = ProcessBuilder(
                "./jmeter.sh -n -t test.jmx -l test_log.jtl"
//                "-stacktrace"
            )

            val resources = ResourcePatternUtils.getResourcePatternResolver(DefaultResourceLoader())
                .getResources("classpath*:api/**")

            val resourceFile = File(resources.toString())

            // jar 파일 경로
            processBuilder.directory(
                resourceFile.parentFile
            )

            val process = processBuilder.start()
            val exitCode = process.waitFor()

            println("Process exitCode: $exitCode")

        } catch (e: Exception) {
            logger.error("Failed to run: ${e.message.toString()}")
        }
    }

}