package loadrover.api.io.domain.task

import loadrover.api.io.config.exception.BaseException
import loadrover.api.io.config.exception.ExceptionCode
import loadrover.api.io.domain.base.BaseDto
import loadrover.api.io.domain.generator.GeneratorEntity
import loadrover.api.io.domain.project.ProjectRepository
import loadrover.api.io.infra.AwsS3Service
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
    private val awsS3Service: AwsS3Service,
) {
    private val logger = LoggerFactory.getLogger(TaskService::class.java)

    fun getTaskList(projectId: Long?): MutableList<TaskDto.TaskListResponse> {
        val responseData: MutableList<TaskDto.TaskListResponse> = mutableListOf()
        val taskList = taskRepository.findByProjectId(projectId)

        for (task in taskList) {
            responseData.add(
                TaskDto.TaskListResponse(
                    taskId = task.id,
                    title = task.title,
                    status = task.status,
                    reportPath = task.reportPath,
                    hostList = task.generatorList.map {
                        TaskDto.GeneratorDto(
                            host = it.hostAddress,
                            type = it.type
                        )
                    }.toMutableSet()
                )
            )
        }

        return responseData
    }

    fun getTaskDetail(taskId: Long): TaskDto.TaskDetailDto {
        val task = taskRepository.findById(taskId).orElseThrow {
            throw BaseException(ExceptionCode.NOT_FOUND_CONTENTS)
        }

        return TaskDto.TaskDetailDto(
            taskId = task.id,
            title = task.title,
            status = task.status,
            uploadFileName = task.uploadFileName,
            host = task.generatorList.map {
                TaskDto.GeneratorDto(
                    host = it.hostAddress,
                    type = it.type
                )
            }.toMutableSet(),
            reportPath = task.reportPath
        )
    }

    @Transactional
    fun createTask(request: TaskDto.TaskCreateRequest, scenarioFile: MultipartFile) {
        val project = projectRepository.findById(request.projectId).orElseThrow {
            throw BaseException(ExceptionCode.NOT_FOUND_CONTENTS)
        }

        val file = BaseDto.FileDto(scenarioFile)

        awsS3Service.upload(file.getUploadPath(), file.upLoadFile.inputStream)

        val task = TaskEntity(
            title = request.title,
            status = TaskStatus.NEW,
            uploadFileName = file.uploadFileName,
            // 파일 내용 미리보기
            description = file.getContentsFromFile(scenarioFile, 500),
            runCount = 0,
            project = project
        )

        val generatorList: MutableList<GeneratorEntity> = mutableListOf()
//        for (generator in request.host) {
//            generatorList.plusAssign(
//                GeneratorEntity(
//                    hostAddress = generator.host,
//                    type = generator.type,
//                    task = task
//                )
//            )
//        }

        task.generatorList = generatorList.toMutableSet()

        try {
            taskRepository.save(task)

        } catch (e: Exception) {
            logger.error("Failed to create task: ${e.message.toString()}, taskTitle: ${request.title}")
            throw BaseException(ExceptionCode.CREATE_FAIL)
        }
    }

    @Transactional
    fun updateTask(request: TaskDto.TaskUpdateRequest) {
        val task = taskRepository.findById(request.taskId).orElseThrow {
            throw BaseException(ExceptionCode.NOT_FOUND_CONTENTS)
        }

        task.title = request.title

        // TODO: task update

        try {
            taskRepository.save(task)

        } catch (e: Exception) {
            logger.error("Failed to update task: ${e.message.toString()}, taskId: ${request.taskId}")
            throw BaseException(ExceptionCode.UPDATE_FAIL)
        }
    }

}