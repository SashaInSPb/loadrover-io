package loadrover.api.io.domain.scenario

import GatlingTestRunner
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
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
    fun createScenario(request: ScenarioDto.RequestProjectDto) {
        return scenarioService.createScenario(request)
    }

    @GetMapping("")
    @Operation(summary = "", description = "")
    fun runSimulation() {
        val runner = GatlingTestRunner("TestSimulation")
        // 원하는 동작을 호출
        runner.runTest()
    }
}