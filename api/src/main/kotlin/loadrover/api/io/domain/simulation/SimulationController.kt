package loadrover.api.io.domain.simulation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import loadrover.api.io.utils.SimulationLogUtils
import org.slf4j.LoggerFactory
import org.springframework.core.io.ResourceLoader
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.*
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/simulation")
@Tag(name = "", description = "/simulation")
class SimulationController(
    private val simulationService: SimulationService,
    private val simulationLogUtils: SimulationLogUtils,
    private val resourceLoader: ResourceLoader
) {
    private val logger = LoggerFactory.getLogger(SimulationService::class.java)

    //TODO: 테스트 진행 상황 알 수 있는 방법 알아보기
    @Async
    @PostMapping("/run")
    @Operation(summary = "", description = "")
    fun runSimulation(@RequestBody request: SimulationDto.RunSimulationRequest): CompletableFuture<ResponseEntity<String>> {

        if (logger.isDebugEnabled) {
            logger.debug("API call received. scenarioId: ${request.scenarioId}")
            simulationLogUtils.createLogFile(request.scenarioId, "Run simulation")
        }

        return CompletableFuture.supplyAsync {

            executeScript(request.scenarioId)

            try {
                // 기존 방식
//                Runtime.getRuntime().exec("./gradlew :gatling:gatlingRun-work.${request.scenarioId} -stacktrace")

                simulationService.moveReadyToProgress(request.scenarioId)
                ResponseEntity.ok("Running simulation, scenarioId: ${request.scenarioId}")
            } catch (e: Exception) {
                simulationLogUtils.createLogFile(request.scenarioId, e.message.toString())
                ResponseEntity.status(500).body("Failed to run simulation, scenarioId: ${request.scenarioId}")
            }
        }
    }

    @GetMapping("/{scenarioId}")
    @Operation(summary = "", description = "Test420240124205133197")
    fun getSimulationResult(@PathVariable("scenarioId") scenarioId: String): SimulationDto.ResultResponse {
        return simulationService.getSimulationResult(scenarioId)
    }

    @Async
    @PostMapping("/retry")
    @Operation(summary = "", description = "")
    fun retrySimulation(@RequestBody request: SimulationDto.RunSimulationRequest){

    }


    private fun executeScript(scenarioId: String) {
        try {
            val processBuilder = ProcessBuilder(
                "./gradlew",
                ":gatling:gatlingRun-work.$scenarioId",
                "-stacktrace"
            )

            val classPathRoot = Paths.get(Thread.currentThread().contextClassLoader.getResource("")!!.toURI()).toFile()
            val rootDirectory = classPathRoot.parentFile.parentFile.parentFile.parentFile.parentFile

            // 프로젝트 root dir로 process 실행 설정
            processBuilder.directory(
                rootDirectory
            )

            val logFile = File("logs/simulation/$scenarioId.log")
            processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile))
            processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(logFile))

            val process = processBuilder.start()
            val exitCode = process.waitFor()

            println("Process exitCode: $exitCode")
        } catch (e: Exception) {
            logger.error("Failed to run: ${e.message.toString()}, scenarioUUID: $scenarioId")
        }
    }

}