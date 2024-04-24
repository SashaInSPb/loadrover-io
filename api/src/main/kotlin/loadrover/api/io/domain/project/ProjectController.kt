package loadrover.api.io.domain.project

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import loadrover.api.io.domain.task.TaskDto
import loadrover.api.io.domain.task.TaskService
import loadrover.api.io.utils.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/project")
class ProjectController(
    private val projectService: ProjectService
) {

    @GetMapping("")
    @Operation(summary = "모든 프로젝트 목록 조회")
    fun getProjectList(@RequestParam(value = "projectId", required = false) projectId: String?, model: Model): String {
        val projectList = projectService.getAllProjectList()
        println("ProjectId: $projectId")

        model.addAttribute("projectList", projectList)
        model.addAttribute("projectListJsonString", StringUtils.objectToJsonString(projectList))

        return "views/project"
    }

    @PostMapping
    @Operation(summary = "프로젝트 생성")
    fun createProject(@RequestBody @Valid request: ProjectDto.ProjectCreateRequest) {
        projectService.createProject(request)
    }

    @PutMapping
    @Operation(summary = "프로젝트 수정")
    fun updateProject(@RequestBody @Valid request: ProjectDto.ProjectUpdateRequest ) {
        projectService.updateProject(request)
    }

}