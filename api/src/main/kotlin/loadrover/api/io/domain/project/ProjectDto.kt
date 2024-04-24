package loadrover.api.io.domain.project

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

class ProjectDto {

    @Schema(description = "프로젝트 리스트 조회 response")
    data class ProjectListResponse(
        val projectId: Long,
        val title: String,
        val clientName: String? = null,
        val taskCount: Int,
        val startDateTime: String? = null,
        val endDateTime: String? = null,
    )

    @Schema(description = "프로젝트 생성 request")
    data class ProjectCreateRequest(
        val title: String,
        val clientName: String? = null,
        val startDateTime: LocalDateTime? = null,
        val endDateTime: LocalDateTime? = null
    )

    @Schema(description = "프로젝트 변경 request")
    data class ProjectUpdateRequest(
        val projectId: Long,
        val title: String,
        val clientName: String? = null,
        val startDateTime: LocalDateTime? = null,
        val endDateTime: LocalDateTime? = null
    )

}
