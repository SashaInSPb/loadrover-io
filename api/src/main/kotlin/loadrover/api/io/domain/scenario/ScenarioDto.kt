package loadrover.api.io.domain.scenario

import java.time.LocalDateTime

class ScenarioDto {

    data class HeaderField(
        val section: String,
        val value: String
    )

    data class RequestScenarioDto(
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

data class FileDto(
    val scenarioTitle: String,
    val scenarioId: String,
    var status: ScenarioStatus,
    var workDate: LocalDateTime? = null
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