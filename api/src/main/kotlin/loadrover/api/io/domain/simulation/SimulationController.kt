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

    @GetMapping("/result")
    @Operation(summary = "", description = "")
    fun getSimulationResult() {
        return simulationService.getSimulationResult()
    }

    // Simulation run 테스트
    @PostMapping("/run")
    @Operation(summary = "", description = "")
    fun buildSimulation(@RequestBody scenarioName: String): String {

        Runtime.getRuntime().exec("gradle gatlingRun-simulations.${scenarioName}")
        // 실행 결과 반환
        return "Test progressing"
    }

}