package loadrover.api.io.domain.scenario

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/scenario")
@Tag(name = "", description = "/scenario")
class ScenarioController(
    private val scenarioService: ScenarioService
) {
    @PostMapping("")
    @Operation(summary = "", description = "")
    fun createScenario(request: ScenarioDto.RequestProjectDto) {
        return scenarioService.createScenario(request)
    }
}