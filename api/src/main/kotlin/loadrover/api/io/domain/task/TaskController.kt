package loadrover.api.io.domain.task

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/task")
@Tag(name = "작업 관리 API", description = "/task")
class TaskController(
    private val taskService: TaskService
) {

    @GetMapping("/list")
    @Operation(summary = "작업 목록 조회")
    fun getTaskList(@RequestParam(value = "projectId", required = false) projectId: String): MutableList<TaskDto.TaskListResponse> {
        return taskService.getTaskList(projectId.toLong())
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "작업 상세 조회")
    fun getTaskDetail(@PathVariable("taskId") taskId: Long): TaskDto.TaskDetailDto {
        return taskService.getTaskDetail(taskId)
    }

    @PostMapping("", consumes = ["multipart/form-data"])
    @Operation(summary = "작업 생성")
    fun createTask(
        @RequestPart("taskCreateRequest") request: TaskDto.TaskCreateRequest,
        @RequestPart("scenarioFile") scenarioFile: MultipartFile
    ) {
        taskService.createTask(request, scenarioFile)
    }

    // 향후
    @PutMapping("", consumes = ["multipart/form-data"])
    @Operation(summary = "작업 수정")
    fun updateTask(
        @RequestPart("taskUpdateRequest") request: TaskDto.TaskUpdateRequest,
        @RequestPart("scenarioFile") scenarioFile: MultipartFile
    ) {
        taskService.updateTask(request)
    }

}