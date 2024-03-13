package loadrover.api.io.domain.project

import loadrover.api.io.config.exception.BaseException
import loadrover.api.io.config.exception.ExceptionCode
import loadrover.api.io.domain.scenario.ScenarioService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectService(
    val projectRepository: ProjectRepository
) {
    private val logger = LoggerFactory.getLogger(ProjectService::class.java)

    fun getAllProjectList(): ProjectDto.AllProjectListResponse {
        val responseData: MutableList<ProjectDto.AllProjectListResponse.ProjectDto> = mutableListOf()
        val projectList = projectRepository.findAll()

        for (project in projectList) {
            responseData.plusAssign(
                ProjectDto.AllProjectListResponse.ProjectDto(
                    projectId = project.id,
                    title = project.title,
                    taskCount = project.taskList.size,
                    startDateTime = project.startDateTime,
                    endDateTime = project.endDateTime
                )
            )
        }

        return ProjectDto.AllProjectListResponse(
            projectList = responseData.sortedBy { it.endDateTime }.toMutableList()
        )
    }

    @Transactional
    fun createProject(request: ProjectDto.ProjectCreateRequest) {
        val project = ProjectEntity(
            title = request.title,
            clientName = request.clientName,
            startDateTime = request.startDateTime,
            endDateTime = request.endDateTime
        )

        try {
            projectRepository.save(project)

        } catch (e: Exception) {
            logger.error("Failed to create project: ${e.message.toString()}, projectTitle: ${request.title}")
            throw BaseException(ExceptionCode.CREATE_FAIL)
        }
    }

    @Transactional
    fun updateProject(request: ProjectDto.ProjectUpdateRequest) {
        val project = projectRepository.findById(request.projectId).orElseThrow{
            throw BaseException(ExceptionCode.NOT_FOUND_CONTENTS)
        }

        project.clientName = request.clientName
        project.title = request.title
        project.startDateTime = request.startDateTime
        project.endDateTime = request.endDateTime

        try {
            projectRepository.save(project)

        } catch (e: Exception) {
            logger.error("Failed to update project: ${e.message.toString()}, projectId: ${request.projectId}")
            throw BaseException(ExceptionCode.UPDATE_FAIL)
        }

    }


}