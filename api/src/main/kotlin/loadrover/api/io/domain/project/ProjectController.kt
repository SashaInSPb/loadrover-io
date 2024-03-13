package loadrover.api.io.domain.project

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/project")
@Tag(name = "프로젝트 관리 API", description = "/project")
class ProjectController(
    private val projectService: ProjectService
) {

    @GetMapping
    @Operation(summary = "모든 프로젝트 목록 조회")
    fun getProjectList(): ProjectDto.AllProjectListResponse {
        return projectService.getAllProjectList()
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