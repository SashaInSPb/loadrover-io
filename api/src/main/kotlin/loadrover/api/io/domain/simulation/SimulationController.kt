//package loadrover.api.io.domain.simulation
//
//import io.swagger.v3.oas.annotations.Operation
//import io.swagger.v3.oas.annotations.tags.Tag
//import loadrover.api.io.config.LoadroverConfig
//import loadrover.api.io.config.exception.ExceptionCode
//import loadrover.api.io.config.exception.NotFoundDataException
//import loadrover.api.io.utils.FileUtils
//import loadrover.api.io.utils.SimulationLogUtils
//import org.slf4j.LoggerFactory
//import org.springframework.http.ResponseEntity
//import org.springframework.scheduling.annotation.Async
//import org.springframework.web.bind.annotation.*
//import java.util.concurrent.CompletableFuture
//
//@RestController
//@RequestMapping("/simulation")
//@Tag(name = "Simulation controller", description = "/simulation")
//class SimulationController(
//    private val simulationService: SimulationService,
//    private val simulationLogUtils: SimulationLogUtils,
//    private val fileUtils: FileUtils,
//    private val loadroverConfig: LoadroverConfig
//) {
//    private val logger = LoggerFactory.getLogger(SimulationService::class.java)
//
//    @Async
//    @PostMapping("/run")
//    @Operation(summary = "부하테스트 실행")
//    fun runSimulation(@RequestBody request: SimulationDto.RunSimulationRequest): CompletableFuture<ResponseEntity<String>> {
//
//        if (logger.isDebugEnabled) {
//            logger.debug("API call received. scenarioId: ${request.scenarioId}")
//            simulationLogUtils.createLogFile(request.scenarioId, "Run simulation")
//        }
//
//        return CompletableFuture.supplyAsync {
//
//            simulationService.executeGatlingScript(request.scenarioId)
//
//            try {
//                // 기존 방식
////                Runtime.getRuntime().exec("./gradlew :gatling:gatlingRun-work.${request.scenarioId} -stacktrace")
//                fileUtils.moveJsonFile(request.scenarioId, loadroverConfig.gatling.source, loadroverConfig.gatling.progress)
//                ResponseEntity.ok("Running simulation, scenarioId: ${request.scenarioId}")
//
//            } catch (e: Exception) {
//                simulationLogUtils.createLogFile(request.scenarioId, e.message.toString())
//                ResponseEntity.status(500).body("Failed to run simulation, scenarioId: ${request.scenarioId}")
//            }
//        }
//    }
//
//    @GetMapping("/{scenarioId}")
//    @Operation(summary = "부하테스트 결과 조회")
//    fun getSimulationResult(@PathVariable("scenarioId") scenarioId: String): SimulationDto.ResultResponse {
//        return simulationService.getSimulationResult(scenarioId)
//    }
//
//    @PostMapping("/retry")
//    @Operation(summary = "부하테스트 재실행")
//    fun retrySimulation(@RequestBody request: SimulationDto.RunSimulationRequest): CompletableFuture<ResponseEntity<String>>{
//        if (logger.isDebugEnabled) {
//            logger.debug("API call received. scenarioId: ${request.scenarioId}")
//            simulationLogUtils.createLogFile(request.scenarioId, "Retry simulation")
//        }
//
//        val resultList = fileUtils.searchResultDirectories()
//        var resultMatchCount = 0
//
//        for (result in resultList) {
//            val mappedSimulationId = result.scenarioId.replace("-\\d+".toRegex(),"")
//
//            if (mappedSimulationId == request.scenarioId) {
//                val resultDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.result}/${result.scenarioId}"
//                fileUtils.deleteDirectory(resultDirectory)
//
//                resultMatchCount += 1
//            }
//        }
//
//        if (resultMatchCount == 0) {
//            throw NotFoundDataException(ExceptionCode.NOT_FOUND_CONTENTS)
//        }
//
//        return CompletableFuture.supplyAsync {
//            simulationService.executeGatlingScript(request.scenarioId)
//
//            try {
//                // json 파일 complete -> progress로 변경
//                fileUtils.moveJsonFile(request.scenarioId, loadroverConfig.gatling.complete, loadroverConfig.gatling.progress)
//                ResponseEntity.ok("Running simulation, scenarioId: ${request.scenarioId}")
//
//            } catch (e: Exception) {
//                simulationLogUtils.createLogFile(request.scenarioId, e.message.toString())
//                ResponseEntity.status(500).body("Failed to run simulation, scenarioId: ${request.scenarioId}")
//            }
//        }
//
//    }
//
//}