package loadrover.api.io.domain.task

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/task")
@Tag(name = "작업 관리 API", description = "/task")
class TaskController(
    private val taskService: TaskService
) {

    @GetMapping("/list/{projectId}")
    @Operation(summary = "작업 목록 조회")
    fun getTaskList(@PathVariable("projectId") projectId: Long): TaskDto.TaskListResponse {
        return taskService.getTaskList(projectId)
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "작업 상세 조회")
    fun getTaskDetail(@PathVariable("taskId") taskId: Long): TaskDto.TaskDetailDto {
        return taskService.getTaskDetail(taskId)
    }

    @PostMapping("", consumes = ["multipart/form-data"])
    @Operation(summary = "작업 생성")
    fun createTask(
        @RequestPart request: TaskDto.TaskCreateRequest,
        @RequestPart("scenarioFile") scenarioFile: MultipartFile
    ) {
        taskService.createTask(request, scenarioFile)
    }

    @PutMapping
    @Operation(summary = "작업 수정")
    fun updateTask(@RequestBody @Valid request: TaskDto.TaskUpdateRequest) {
        taskService.updateTask(request)
    }

}