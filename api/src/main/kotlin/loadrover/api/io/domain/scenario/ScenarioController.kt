package loadrover.api.io.domain.scenario

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/scenario")
@Tag(name = "Scenario controller", description = "/scenario")
class ScenarioController(
    private val scenarioService: ScenarioService,
) {
    @GetMapping("/{scenarioId}")
    @Operation(summary = "시나리오 조회")
    fun getScenarioContent(@PathVariable("scenarioId") scenarioId: String): ScenarioDto.ResponseScenarioDto? {
        return scenarioService.getScenarioContent(scenarioId)
    }
    @PostMapping("")
    @Operation(summary = "시나리오 생성")
    fun createScenario(@RequestBody request: ScenarioDto.ScenarioCreateDto){
        return scenarioService.createScenario(request)
    }

    @PutMapping("")
    @Operation(summary = "시나리오 수정")
    fun reviseScenario(@RequestBody request: ScenarioDto.ScenarioReviseDto){
        return scenarioService.reviseScenario(request)
    }

    @GetMapping("/list")
    @Operation(summary = "등록된 시나리오 리스트 조회")
    fun getScenarioList(): MutableSet<FileDto> {
        return scenarioService.getFileList()
    }
}