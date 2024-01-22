package loadrover.api.io.domain.scenario

import java.time.LocalDateTime

class ScenarioDto {

    data class HeaderField(
        val section: String,
        val value: String
    )

    data class ScenarioControl(
        var idxGetRequest: Int = 0,
        var idxPostRequest: Int = 0,
        var idxPause: Int = 0
    ) {
        fun tickGetRequest() {
            idxGetRequest++
        }

        fun tickPostRequest() {
            idxPostRequest++
        }

        fun tickPause() {
            idxPause++
        }
    }

    data class RequestScenarioDto(
        val scenario: ScenarioPropertyDto,
        val target: TargetPropertyDto,
        val auth: AuthPropertyDto,
        val schedule: Schedule
    ) {
        data class ScenarioPropertyDto(
            val name: String,
            val concurrent: Short
        )

        data class TargetPropertyDto(
            val host: String
        )

        data class AuthPropertyDto(
            val policy: String?
        )
    }

    data class Schedule(
        val orderBook: List<String>,
        val getRequest: List<ScheduleTypeGetRequest?>,
        val postRequest: List<ScheduleTypePostRequest?>,
        val pause: List<ScheduleTypePause?>
    ) {
        data class ScheduleTypeGetRequest(
            val endpoint: String
        )

        data class ScheduleTypePostRequest(
            val endpoint: String,
            val payload: String
        )

        data class ScheduleTypePause(
            val sec: Short
        )
    }
}

data class FileDto(
    val scenarioId: String,
    var status: ScenarioStatus,
    var workDate: LocalDateTime? = null
)

enum class HttpHeaderSection(
    val value: String
) {
    USER_AGENT("UserAgent"),
    ACCEPT("Accept"),
    AUTHORIZATION("authorization")
}

enum class UserAgent(
    val value: String
) {
    CHROME_114("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"),
    FIREFOX_114("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/114.0"),
}

enum class ScenarioAction (
    val value: String
) {
    GetRequest("GET_REQUEST"),
    PostRequest("POST_REQUEST"),
    Pause("PAUSE")
}

// File로 관리되는 시나리오 상태값
enum class ScenarioStatus {
    PRECONVERSION, READY, PROGRESS, COMPLETE, STOP
}