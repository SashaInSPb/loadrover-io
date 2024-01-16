package loadrover.api.io.domain.scenario

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/scenario")
@Tag(name = "", description = "/scenario")
class ScenarioController(
    private val scenarioService: ScenarioService,
) {
    @PostMapping("")
    @Operation(summary = "", description = "")
    fun createScenario(@RequestBody request: ScenarioDto.RequestProjectDto) {
        return scenarioService.createScenario(request)
    }

    @PutMapping("")
    @Operation(summary = "", description = "")
    fun moveSimulationToProgress() {
        return scenarioService.moveSimulationToProgress()
    }

    @GetMapping("")
    @Operation(summary = "", description = "")
    fun getScenarioList() {
        return scenarioService.getScenarioList()
    }

//    // Simutlation run 테스트
//    @GetMapping("")
//    @Operation(summary = "", description = "")
//    fun runSimulation() {
//        val runner = GatlingTestRunner("TestSimulation")
//        // 원하는 동작을 호출
//        runner.runTest()
//    }
}