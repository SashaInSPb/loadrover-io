package loadrover.api.io.domain.simulation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/simulation")
@Tag(name = "", description = "/simulation")
class SimulationController(
    private val simulationService: SimulationService
) {
    private val log = LoggerFactory.getLogger(SimulationService::class.java)

    //TODO: 테스트 진행상황 알 수 있는 방법 알아보기
    @PostMapping("/run")
    @Operation(summary = "", description = "")
    fun runSimulation(@RequestBody request: SimulationDto.RunSimulationRequest): String {

        try {

            val process = Runtime.getRuntime().exec("gradle gatlingRun-work.${request.scenarioId} -stacktrace")
            val timeoutInMillis: Long = 1000


//            if (process.waitFor(timeoutInMillis, TimeUnit.MILLISECONDS)) {
//                simulationService.moveWorkToProgress(request.scenarioId)
//            }

        } catch (e: Error) {
            log.error("Failed to run simulation: ${e.message}, scenarioId: ${request.scenarioId}")
        }

        // 실행 결과 반환 실패 뱉기
        return "Test progressing"
    }

    @GetMapping("/{scenarioId}")
    @Operation(summary = "", description = "Test420240124205133197")
    fun getSimulationResult(@PathVariable("scenarioId") scenarioId: String): SimulationDto.ResultResponse {
        return simulationService.getSimulationResult(scenarioId)
    }

}