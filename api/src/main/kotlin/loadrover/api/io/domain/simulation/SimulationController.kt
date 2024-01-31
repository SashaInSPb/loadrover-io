package loadrover.api.io.domain.simulation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import loadrover.api.io.utils.logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/simulation")
@Tag(name = "", description = "/simulation")
class SimulationController(
    private val simulationService: SimulationService
) {
    private val log = LoggerFactory.getLogger(SimulationService::class.java)

    //TODO: 테스트 진행 상황 알 수 있는 방법 알아보기
    @Async
    @PostMapping("/run")
    @Operation(summary = "", description = "")
    fun runSimulation(@RequestBody request: SimulationDto.RunSimulationRequest): CompletableFuture<ResponseEntity<String>> {

        if (logger().isDebugEnabled) {
            logger().debug("API call received. scenarioId: ${request.scenarioId}")
        }

        return CompletableFuture.supplyAsync {
            try {
                Runtime.getRuntime().exec("./gradlew :gatling:gatlingRun-work.${request.scenarioId} -stacktrace")

                // dev 브랜치에서 실행 코드
//                Runtime.getRuntime().exec("gradle gatlingRun-work.${request.scenarioId} -stacktrace")

                simulationService.moveReadyToProgress(request.scenarioId)
                ResponseEntity.ok("Running simulation, scenarioId: ${request.scenarioId}")
            } catch (e: Exception) {
                println("Failed to run simulation: ${e.message}, scenarioId: ${request.scenarioId}")

                ResponseEntity.status(500).body("Failed to run simulation, scenarioId: ${request.scenarioId}")
            }
        }
    }

    @GetMapping("/{scenarioId}")
    @Operation(summary = "", description = "Test420240124205133197")
    fun getSimulationResult(@PathVariable("scenarioId") scenarioId: String): SimulationDto.ResultResponse {
        return simulationService.getSimulationResult(scenarioId)
    }

}