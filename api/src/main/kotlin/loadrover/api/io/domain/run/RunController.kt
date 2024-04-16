package loadrover.api.io.domain.run

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/run")
@Tag(name = "Run controller", description = "/run")
class RunController(
    private val runService: RunService
) {
    // TODO: runId를 던져주자
    @GetMapping("/{taskId}")
    @Operation(summary = "부하테스트 실행")
    fun runSimulation(@PathVariable("taskId") taskId: Long) {
        runService.runSimulation(taskId)
    }

}