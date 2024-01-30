package loadrover.api.io.domain.scenario

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Service
class ScenarioService(
    private val loadroverConfig: LoadroverConfig,
    private val fileUtils: FileUtils
) {
    private val log = LoggerFactory.getLogger(ScenarioService::class.java)

    fun getFileList(): MutableSet<FileDto> {

        val sourceFileList = fileUtils.searchFiles("source")
        val progressFileList = fileUtils.searchFiles("progress")
        val completeFileList = fileUtils.searchFiles("complete")

        val result = sourceFileList
            .union(progressFileList)
            .union(completeFileList)

        return result.toMutableSet()
    }

    fun createScenario(request: ScenarioDto.RequestScenarioDto) {
        val scenarioUUID = getUUID()
        val scenarioClass = "${request.task.name}${scenarioUUID}"

        saveSourceFile(request, scenarioUUID, scenarioClass)

        val userAgent = UserAgent.CHROME_114
        val host = request.task.targetHost

        val codes = StringBuilder()
        codes.append("package work\n\n")

        codes.append("import io.gatling.javaapi.core.*\n")
        codes.append("import io.gatling.javaapi.core.CoreDsl.*\n")
        codes.append("import io.gatling.javaapi.http.HttpDsl.*\n\n")

        codes.append("class ${scenarioClass}: Simulation() {\n")
        codes.append("val httpProtocol = http\n")
        codes.append("    .baseUrl(\"${host}\")\n")
        codes.append("    .inferHtmlResources()\n")
        codes.append("    .acceptEncodingHeader(\"gzip, deflate, br\")\n")
        codes.append("    .acceptLanguageHeader(\"ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\")\n")
        // 중복
//        codes.append("    .userAgentHeader(\"${userAgent.value}\")\n")

        val headerIdx = 0
        val headerId = "headers_${headerIdx}"
        val headers: List<ScenarioDto.HeaderField> = getHeader(userAgent)
        codes.append("val $headerId: MutableMap<CharSequence, String> = HashMap()\n")

        codes.append("init {\n")

        for (headerField in headers) {
            codes.append("$headerId.put(\"${headerField.section}\",\"${headerField.value}\")\n")
        }

        codes.append("val scn = scenario(\"${request.task.name}$scenarioUUID\")\n")

        // account list를 돌면서 시나리오 생성
        // TODO: account list가 empty일 경우, 아래 api를 타지 않는다...
        if (request.accountList.isNotEmpty()) {
            for (account in request.accountList) {
                // TODO: process 순서 보장 필요
                for (action in request.process) {
                    when (action.value.apiType) {
                        ApiType.GET -> {
                            codes.append(".exec(")
                            codes.append("http(\"request_${action.value.apiType}\")")
                            codes.append(".get(\"${action.value.apiUrl}\")")
                            codes.append(".headers($headerId)")
                            codes.append(")\n")
                            codes.append(".pause(${action.value.pause})\n")
                        }

                        ApiType.POST -> {
                            var payload = action.value.params.replace("\"", "\\\"")

                            // 로그인 시, payload 작성
                            if (action.value.apiUrl.contains("authentication")) {
                                payload =
                                    "{\"email\":\"${account.id}\",\"password\":\"${account.password}\",\"loginSite\":\"LPM\"}".replace(
                                        "\"",
                                        "\\\""
                                    )
                            }

                            codes.append(".exec(")
                            codes.append(("http(\"request_${action.value.apiType}\")"))
                            codes.append(".post(\"${action.value.apiUrl}\")")
                            codes.append(".headers($headerId)")
                            codes.append(".body(StringBody(\"${payload}\"))")

                            // 로그인 후, access token 추출
                            if (action.value.apiUrl.contains("authentication")) {
                                codes.append(".check(jsonPath(\"$.accessToken\").saveAs(\"accessToken\")))\n")
                            }
                            codes.append(".pause(${action.value.pause})\n")

                            // 로그인 시, header에 access 토큰 추가, 중간에 끼면 안됨
//                        if (action.value.apiUrl.contains("authentication")) {
//                            codes.append("$headerId.put(\"${HttpHeaderSection.AUTHORIZATION.value}\",\"bearer #accessToken\")\n")
//                        }
                        }

                        ApiType.PUT -> {
                            val payload = action.value.params.replace("\"", "\\\"")

                            codes.append(".exec(")
                            codes.append(("http(\"request_${action.value.apiType}\")"))
                            codes.append(".put(\"${action.value.apiUrl}\")")
                            codes.append(".headers($headerId)")
                            codes.append(".body(StringBody(\"${payload}\"))")
                            codes.append(")\n")
                            codes.append(".pause(${action.value.pause})\n")
                        }

                        ApiType.DELETE -> {
                            codes.append(".exec(")
                            codes.append(("http(\"request_${action.value.apiType}\")"))
                            codes.append(".delete(\"${action.value.apiUrl}\")")
                            codes.append(".headers($headerId)")
                            codes.append(")")
                            codes.append(".pause(${action.value.pause})\n")
                        }
                    }
                }
            }
        } else {
            for (action in request.process) {
                when (action.value.apiType) {
                    ApiType.GET -> {
                        codes.append(".exec(")
                        codes.append("http(\"request_${action.value.apiType}\")")
                        codes.append(".get(\"${action.value.apiUrl}\")")
                        codes.append(".headers($headerId)")
                        codes.append(")\n")
                        codes.append(".pause(${action.value.pause})\n")
                    }

                    ApiType.POST -> {
                        val payload = action.value.params.replace("\"", "\\\"")

                        codes.append(".exec(")
                        codes.append(("http(\"request_${action.value.apiType}\")"))
                        codes.append(".post(\"${action.value.apiUrl}\")")
                        codes.append(".headers($headerId)")
                        codes.append(".body(StringBody(\"${payload}\"))")

                        // 로그인 후, access token 추출
                        if (action.value.apiUrl.contains("authentication")) {
                            codes.append(".check(jsonPath(\"$.accessToken\").saveAs(\"accessToken\")))\n")
                        }
                        codes.append(".pause(${action.value.pause})\n")

                    }

                    ApiType.PUT -> {
                        val payload = action.value.params.replace("\"", "\\\"")

                        codes.append(".exec(")
                        codes.append(("http(\"request_${action.value.apiType}\")"))
                        codes.append(".put(\"${action.value.apiUrl}\")")
                        codes.append(".headers($headerId)")
                        codes.append(".body(StringBody(\"${payload}\"))")
                        codes.append(")\n")
                        codes.append(".pause(${action.value.pause})\n")
                    }

                    ApiType.DELETE -> {
                        codes.append(".exec(")
                        codes.append(("http(\"request_${action.value.apiType}\")"))
                        codes.append(".delete(\"${action.value.apiUrl}\")")
                        codes.append(".headers($headerId)")
                        codes.append(")")
                        codes.append(".pause(${action.value.pause})\n")
                    }
                }
            }
        }

        codes.append("this.setUp(scn.injectOpen(atOnceUsers(${request.task.concurrent}))).protocols(httpProtocol)\n")
        codes.append("}}\n")

        val saveWorkPath = "${loadroverConfig.gatling.workPath}/${loadroverConfig.gatling.work}/${scenarioClass}.kt"

        try {
            val codeString: String = codes.toString()
            File(saveWorkPath).bufferedWriter().use {
                it.write(codeString)
            }
        } catch (e: Exception) {
            log.error("Failed to create: ${e.message}, scenarioUUID: $scenarioUUID")
        }
    }

    private fun getUUID(): String {
        val dataFormat = SimpleDateFormat("yyyyMMddHHmmssSSS")
        return dataFormat.format(Date()).toString()
    }

    private fun getHeader(agentType: UserAgent): List<ScenarioDto.HeaderField> {
        val headers: MutableList<ScenarioDto.HeaderField> = mutableListOf()

        when (agentType) {
            UserAgent.CHROME_114 -> headers.add(
                ScenarioDto.HeaderField(
                    HttpHeaderSection.USER_AGENT.value,
                    UserAgent.CHROME_114.value
                )
            )
            UserAgent.FIREFOX_114 -> headers.add(
                ScenarioDto.HeaderField(
                    HttpHeaderSection.USER_AGENT.value,
                    UserAgent.FIREFOX_114.value
                )
            )
        }

        headers.add(
            ScenarioDto.HeaderField(
                HttpHeaderSection.ACCEPT.value,
                "application/json, text/plain, */*")
        )

        headers.add(
            ScenarioDto.HeaderField(
                HttpHeaderSection.CONTENT_TYPE.value,
                "application/json")
        )

        return headers.toList()
    }

    private fun getAuthorization(headers: MutableList<ScenarioDto.HeaderField>, jwtToken: String?) {
        if (!jwtToken.isNullOrEmpty()) {
            headers.add(ScenarioDto.HeaderField(HttpHeaderSection.AUTHORIZATION.value, "bearer $jwtToken"))
        }
    }

    // request json 파일로 저장
    private fun saveSourceFile(request: ScenarioDto.RequestScenarioDto, scenarioUUID: String, scenarioClass: String) {
        val savePath = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.source}/${scenarioClass}.json"
        val serializedObject = jacksonObjectMapper().writeValueAsString(request)

        try {
            File(savePath).bufferedWriter().use {
                it.write(serializedObject)
            }
        } catch (e: Exception) {
            log.error("Failed to save JSON source file, ScenarioUUID: $scenarioUUID")
        }
    }


}