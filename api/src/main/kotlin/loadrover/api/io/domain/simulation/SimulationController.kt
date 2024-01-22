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
    @PostMapping("/run")
    @Operation(summary = "", description = "")
    fun buildSimulation(@RequestBody scenarioName: String): String {
        Runtime.getRuntime().exec("gradle gatlingRun-simulations.${scenarioName}")
        // 실행 결과 반환
        return "Test progressing"
    }

    // TODO: Scheduler, 디렉토리 옮기기
    // result 내 scenarioId와 progress 내 id를 비교 후, 일치하는 id가 있다면 progress 내 파일 삭제
    @PutMapping("")
    @Operation(summary = "", description = "")
    fun moveSimulationToProgress() {
        return simulationService.moveProgressToResult()
    }

}