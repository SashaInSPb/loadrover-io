package loadrover.api.io.domain.scenario

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/scenario")
@Tag(name = "", description = "/scenario")
class ScenarioController(
    private val scenarioService: ScenarioService,
) {

    // 스케쥴러 이용
//    @PostMapping("", consumes = ["multipart/form-data"])
    @PostMapping("")
    @Operation(summary = "", description = "")
    fun createScenario(@RequestBody request: ScenarioDto.RequestScenarioDto){
        return scenarioService.createScenario(request)
    }

    @PutMapping("")
    @Operation(summary = "", description = "")
    fun moveSimulationToProgress(fileName: String) {
        return scenarioService.moveWorkToProgress(fileName)
    }

    @GetMapping("/list")
    @Operation(summary = "", description = "")
    fun getScenarioList(): MutableSet<FileDto> {
        return scenarioService.getFileList()
    }

    // 스케쥴러 이용
//    @PostMapping("")
//    @Operation(summary = "", description = "")
//    fun saveScenarioAsJSON(@RequestBody request: ScenarioDto.RequestScenarioDto) {
//        return scenarioService.saveSourceFile(request)
//    }
}