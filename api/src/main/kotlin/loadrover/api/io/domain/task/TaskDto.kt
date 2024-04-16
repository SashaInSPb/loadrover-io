package loadrover.api.io.domain.task

import io.swagger.v3.oas.annotations.media.Schema
import loadrover.api.io.domain.generator.Type
import org.springframework.web.multipart.MultipartFile

class TaskDto {

    @Schema(description = "작업 리스트 조회 response")
    data class TaskListResponse(
        @Schema(description = "작업 list")
        val taskList: MutableList<TaskDetailDto> = mutableListOf()
    )

    @Schema(description = "작업 상세 조회 response")
    data class TaskDetailDto(
        val title: String,
        val status: TaskStatus,
        val uploadFileName: String? = null,
        val reportPath: String? = null,
        val host: MutableSet<GeneratorDto>,
        val description: String? = null
    )

    @Schema(description = "작업 생성 request")
    data class TaskCreateRequest(
        val projectId: Long,
        val title: String,
        val host: MutableSet<GeneratorDto>
    )

    @Schema(description = "작업 수정 request")
    data class TaskUpdateRequest(
        val taskId: Long,
        val title: String,
        val file: MultipartFile,
        val host: MutableSet<GeneratorDto>
    )

    @Schema(description = "작업 generator 정보")
    data class GeneratorDto(
        val host: String,
        val type: Type
    )
}