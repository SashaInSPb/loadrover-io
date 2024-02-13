package loadrover.api.io.domain.simulation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.config.exception.ExceptionCode
import loadrover.api.io.config.exception.NotFoundDataException
import loadrover.api.io.utils.FileUtils
import loadrover.api.io.utils.SimulationLogUtils
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.*
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/simulation")
@Tag(name = "Simulation controller", description = "/simulation")
class SimulationController(
    private val simulationService: SimulationService,
    private val simulationLogUtils: SimulationLogUtils,
    private val fileUtils: FileUtils,
    private val loadroverConfig: LoadroverConfig
) {
    private val logger = LoggerFactory.getLogger(SimulationService::class.java)

    @Async
    @PostMapping("/run")
    @Operation(summary = "부하테스트 실행")
    fun runSimulation(@RequestBody request: SimulationDto.RunSimulationRequest): CompletableFuture<ResponseEntity<String>> {

        if (logger.isDebugEnabled) {
            logger.debug("API call received. scenarioId: ${request.scenarioId}")
            simulationLogUtils.createLogFile(request.scenarioId, "Run simulation")
        }

        return CompletableFuture.supplyAsync {

            executeGatlingScript(request.scenarioId)

            try {
                // 기존 방식
//                Runtime.getRuntime().exec("./gradlew :gatling:gatlingRun-work.${request.scenarioId} -stacktrace")
                fileUtils.moveJsonFile(request.scenarioId, loadroverConfig.gatling.source, loadroverConfig.gatling.progress)
                ResponseEntity.ok("Running simulation, scenarioId: ${request.scenarioId}")

            } catch (e: Exception) {
                simulationLogUtils.createLogFile(request.scenarioId, e.message.toString())
                ResponseEntity.status(500).body("Failed to run simulation, scenarioId: ${request.scenarioId}")
            }
        }
    }

    @GetMapping("/{scenarioId}")
    @Operation(summary = "부하테스트 결과 조회")
    fun getSimulationResult(@PathVariable("scenarioId") scenarioId: String): SimulationDto.ResultResponse {
        return simulationService.getSimulationResult(scenarioId)
    }

    @Async
    @PostMapping("/retry")
    @Operation(summary = "부하테스트 재실행")
    fun retrySimulation(@RequestBody request: SimulationDto.RunSimulationRequest): CompletableFuture<ResponseEntity<String>>{
        if (logger.isDebugEnabled) {
            logger.debug("API call received. scenarioId: ${request.scenarioId}")
            simulationLogUtils.createLogFile(request.scenarioId, "Retry simulation")
        }

        // result에 있는 폴더 삭제
        val resultList = fileUtils.searchResultFolders()
        var resultMatchCount = 0

        for (result in resultList) {
            val mappedSimulationId = result.scenarioId.replace("-\\d+".toRegex(),"")

            if (mappedSimulationId == request.scenarioId) {
                val resultDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.result}/${result.scenarioId}"
                fileUtils.deleteFile(resultDirectory)

                resultMatchCount += 1
            }
        }

        if (resultMatchCount == 0) {
            throw NotFoundDataException(ExceptionCode.NOT_FOUND_CONTENTS)
        }

        return CompletableFuture.supplyAsync {
            executeGatlingScript(request.scenarioId)

            try {
                // json 파일 complete -> progress로 변경
                fileUtils.moveJsonFile(request.scenarioId, loadroverConfig.gatling.complete, loadroverConfig.gatling.progress)
                ResponseEntity.ok("Running simulation, scenarioId: ${request.scenarioId}")

            } catch (e: Exception) {
                simulationLogUtils.createLogFile(request.scenarioId, e.message.toString())
                ResponseEntity.status(500).body("Failed to run simulation, scenarioId: ${request.scenarioId}")
            }
        }

    }

    // gatling shell script 실행 함수
    private fun executeGatlingScript(scenarioId: String) {
        try {
            val processBuilder = ProcessBuilder(
                "./gradlew",
                ":gatling:gatlingRun-work.$scenarioId",
                "-stacktrace"
            )

            val classPathRoot = Paths.get(Thread.currentThread().contextClassLoader.getResource("")!!.toURI()).toFile()
            logger.debug("ClassPath: $classPathRoot")
            val rootDirectory = classPathRoot.parentFile.parentFile.parentFile.parentFile.parentFile
            logger.debug("RootDirectory: $rootDirectory")

            // 프로젝트 root dir로 process 실행 설정
            processBuilder.directory(
                rootDirectory
            )

            // log 설정
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