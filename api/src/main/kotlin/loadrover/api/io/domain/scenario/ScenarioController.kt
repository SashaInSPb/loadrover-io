package loadrover.api.io.domain.scenario

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/scenario")
@Tag(name = "", description = "/scenario")
class ScenarioController(
    private val scenarioService: ScenarioService,
) {
    @PostMapping("")
    @Operation(summary = "", description = "")
    fun createScenario(@RequestBody request: ScenarioDto.RequestScenarioDto){
        return scenarioService.createScenario(request)
    }

    @GetMapping("/list")
    @Operation(summary = "", description = "")
    fun getScenarioList(): MutableSet<FileDto> {
        return scenarioService.getFileList()
    }

}