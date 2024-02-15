package loadrover.api.io.domain.scenario

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

class ScenarioDto {

    @Schema(description = "시나리오 생성 request")
    data class ScenarioCreateDto(
        val task: TaskDetail,
        val process: Map<Int, RequestDetail> = mapOf(),
        val accountList: List<AccountDetail> = listOf()
    )

    @Schema(description = "시나리오 수정 request")
    data class ScenarioReviseDto(
        val scenarioId: String,
        val task: TaskDetail,
        val process: Map<Int, RequestDetail> = mapOf(),
        val accountList: List<AccountDetail> = listOf()
    ) {
        fun removeScenarioId(): ScenarioCreateDto {
            return ScenarioCreateDto(
                task = this.task,
                process = this.process,
                accountList = this.accountList
            )
        }
    }

    data class TaskDetail(
        val name: String,
        val targetHost: String,
        val concurrent: Int,
        val authType: AuthType,
        val jwtObjectName: String
    )

    data class RequestDetail(
        val apiType: ApiType,
        val apiUrl: String,
        val loginUse: Boolean,
        val params: String,
        val pause: Int? = 0
    )

    data class AccountDetail(
        val id: String,
        val password: String
    )

    @Schema(description = "시나리오 조회 response")
    data class ResponseScenarioDto(
        val task: TaskDetail,
        val process: Map<Int, RequestDetail> = mapOf(),
        val accountList: List<AccountDetail> = listOf()
    ) {

        data class TaskDetail(
            val name: String,
            val targetHost: String,
            val concurrent: Int,
            val authType: AuthType,
            val jwtObjectName: String
        )

        data class RequestDetail(
            val apiType: ApiType,
            val apiUrl: String,
            val loginUse: Boolean,
            val params: String,
            val pause: Int? = 0
        )

        data class AccountDetail(
            val id: String,
            val password: String
        )
    }
}

@Schema(description = "시나리오 리스트 dto")
data class FileDto(
    val scenarioTitle: String,
    val scenarioId: String,
    var status: ScenarioStatus,
    var workDate: LocalDateTime? = null
)

data class HeaderField(
    val section: String,
    val value: String
)

enum class HttpHeaderSection(
    val value: String
) {
    USER_AGENT("UserAgent"),
    ACCEPT("accept"),
    CONTENT_TYPE("Content-Type"),
    AUTHORIZATION("Authorization")
}

enum class UserAgent(
    val value: String
) {
    CHROME_114("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"),
    FIREFOX_114("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/114.0"),
}

// File System으로 관리되는 시나리오 상태값
enum class ScenarioStatus {
    PRE_CONVERSION, READY, PROGRESS, COMPLETE, STOP
}

enum class ApiType {
    GET, POST, PUT, DELETE
}

enum class AuthType {
    JWT, BASIC
}