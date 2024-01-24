package loadrover.api.io.domain.simulation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/simulation")
@Tag(name = "", description = "/simulation")
class SimulationController(
    private val simulationService: SimulationService
) {

    @GetMapping("/{scenarioId}")
    @Operation(summary = "", description = "")
    fun getSimulationResult(@PathVariable("scenarioId") scenarioId: String): SimulationDto.ResultResponse {
        return simulationService.getSimulationResult(scenarioId)
    }

    // Simulation build & run 테스트
    //TODO: process 디렉토리로 옮기기
    @PostMapping("/run")
    @Operation(summary = "", description = "")
    fun buildSimulation(@RequestBody scenarioName: String): String {

        try {
            Runtime.getRuntime().exec("gradle gatlingRun-work.${scenarioName}")
        } catch (e: Error) {
            println("Error: $e")
        }
        // 실행 결과 반환
        return "Test progressing"
    }
}