package loadrover.api.io.domain.run

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import loadrover.api.io.config.LoadroverProperties
import loadrover.api.io.domain.base.BaseDto
import loadrover.api.io.utils.FileUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/run")
@Tag(name = "Run controller", description = "/run")
class RunController(
    private val runService: RunService,
    private val fileUtils: FileUtils,
    private val loadroverProperties: LoadroverProperties,
) {
    // TODO: runId를 던져주자
    @GetMapping("/{taskId}")
    @Operation(summary = "부하테스트 실행")
    fun runSimulation(@PathVariable("taskId") taskId: Long) {
        runService.runSimulation(taskId)
    }

    @GetMapping("")
    fun test(): MutableSet<BaseDto.ResultDto>  {
        return fileUtils.searchDirectories(loadroverProperties.reportDirectory)
    }

}