package loadrover.api.io.domain.scenario

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.springframework.stereotype.Service
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.math.ceil

@Service
class ScenarioService(
    private val loadroverConfig: LoadroverConfig,
    private val fileUtils: FileUtils
) {
    private val logger = LoggerFactory.getLogger(ScenarioService::class.java)

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
        val scenarioClass = "${request.task.name.lowercase()}${scenarioUUID}"

        saveSourceFile(request, scenarioUUID, scenarioClass)

        val userAgent = UserAgent.CHROME_114
        val host = request.task.targetHost

        val codes = StringBuilder()
        codes.append("package work\n\n")

        codes.append("import io.gatling.javaapi.core.*\n")
        codes.append("import io.gatling.javaapi.core.CoreDsl.*\n")
        codes.append("import io.gatling.javaapi.http.HttpDsl.*\n")
        codes.append("import java.lang.Exception\n")
        codes.append("\n")

        codes.append("class ${scenarioClass}: Simulation() {\n")
        codes.append("val httpProtocol = http\n")
        codes.append("    .baseUrl(\"${host}\")\n")
        codes.append("    .inferHtmlResources()\n")
        codes.append("    .acceptEncodingHeader(\"gzip, deflate, br\")\n")
        codes.append("    .acceptLanguageHeader(\"ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\")\n")
        codes.append("    .userAgentHeader(\"${userAgent.value}\")\n")
        codes.append("\n")

        val headerIdx = 0
        val headerId = "headers_${headerIdx}"

        codes.append("init {\n")

        // 상기 httpProtocol 메서드로 처리 가능할지 확인
        codes.append("var ${headerId}: Map<String, String> = mutableMapOf(\n")
        codes.append("\"accept\" to \"application/json, text/plain, */*\",\n")
        codes.append("\"Content-Type\" to \"application/json\"\n")
        codes.append(")\n")

        val accountListSize = request.accountList.size
        for (idx in 0 until accountListSize + 1) {

            // account List 내 원소가 없을 때,
            if (idx > accountListSize - 1 && idx != 0) {
                break
            }

            codes.append("val scn${idx} = scenario(\"${request.task.name}$scenarioUUID-$idx\")\n")

            for (action in request.process) {
                when (action.value.apiType) {
                    ApiType.GET -> {
                        codes.append(".exec(")
                        codes.append("http(\"request_${action.value.apiType}_${idx}\")")
                        codes.append(".get(\"${action.value.apiUrl}\")")
                        codes.append(".headers($headerId)")
                        if (request.accountList.isNotEmpty()) {
                            codes.append(".header(\"${HttpHeaderSection.AUTHORIZATION.value}\", \"bearer #{accessToken${idx}}\")")
                        }

                        codes.append(")\n")
                        codes.append(".pause(${action.value.pause})\n")
                    }

                    ApiType.POST -> {
                        var payload = action.value.params
                            .replace("\n", "")
                            .replace("\"","\\\"")
                            .replace("\\s".toRegex(), "")

                        // 로그인용 payload 작성
                        if (action.value.loginUse && request.accountList.isNotEmpty()) {
                            payload =
                                "{\"email\":\"${request.accountList[idx].id}\",\"password\":\"${request.accountList[idx].password}\",\"loginSite\":\"LPM\"}".replace(
                                    "\"",
                                    "\\\""
                                )
                        }

                        codes.append(".exec(")
                        codes.append("http(\"request_${action.value.apiType}_${idx}\")")
                        codes.append(".post(\"${action.value.apiUrl}\")")
                        codes.append(".headers($headerId)")
                        if (!action.value.loginUse && request.accountList.isNotEmpty()) {
                            codes.append(".header(\"${HttpHeaderSection.AUTHORIZATION.value}\", \"bearer #{accessToken${idx}}\")")
                        }

                        codes.append(".body(StringBody(\"${payload}\"))")
                        if (action.value.loginUse) {
                            codes.append(".check(jsonPath(\"$.accessToken\").saveAs(\"accessToken${idx}\"))\n")
                        }

                        codes.append(")\n")
                        codes.append(".pause(${action.value.pause})\n")
                    }

                    ApiType.PUT -> {
                        val payload = action.value.params
                            .replace("\n", "")
                            .replace("\"","\\\"")
                            .replace("\\s".toRegex(), "")

                        codes.append(".exec(")
                        codes.append(("http(\"request_${action.value.apiType}_${idx}\")"))
                        codes.append(".put(\"${action.value.apiUrl}\")")
                        codes.append(".headers($headerId)")
                        if (request.accountList.isNotEmpty()) {
                            codes.append(".header(\"${HttpHeaderSection.AUTHORIZATION.value}\", \"bearer #{accessToken${idx}}\")")
                        }

                        codes.append(".body(StringBody(\"${payload}\"))")
                        codes.append(")\n")
                        codes.append(".pause(${action.value.pause})\n")
                    }

                    ApiType.DELETE -> {
                        codes.append(".exec(")
                        codes.append(("http(\"request_${action.value.apiType}_${idx}\")"))
                        codes.append(".delete(\"${action.value.apiUrl}\")")
                        codes.append(".headers($headerId)\n")
                        if (request.accountList.isNotEmpty()) {
                            codes.append(".header(\"${HttpHeaderSection.AUTHORIZATION.value}\", \"bearer #{accessToken${idx}}\")")
                        }
                        codes.append(")\n")
                        codes.append(".pause(${action.value.pause})\n")
                    }
                }
            }
        }

        codes.append("try {\n")
        codes.append("this.setUp(\n")
        val concurrentUsers = if (accountListSize == 0) request.task.concurrent else ceil(request.task.concurrent.toDouble() / accountListSize).toInt()

        for (idx in 0 until accountListSize + 1) {
            // account List 내 원소가 없을 때,
            if (idx > accountListSize - 1 && idx != 0) {
                break
            }
            codes.append("scn$idx.injectOpen(atOnceUsers(${concurrentUsers})).protocols(httpProtocol),\n")
        }
        codes.append(")\n")
        codes.append("} catch(e: Exception) {\n")
        codes.append("println(\"Error during scenario setup: \${e.message}\")")
        codes.append("}\n")
        codes.append("}\n")
        codes.append("}\n")

        val saveWorkPath = "${loadroverConfig.gatling.workPath}/${loadroverConfig.gatling.work}/${scenarioClass}.kt"

        try {
            val codeString: String = codes.toString()

            File(saveWorkPath).bufferedWriter().use {
                it.write(codeString)
            }
        } catch (e: Exception) {
            logger.error("Failed to create: ${e.message.toString()}, scenarioUUID: $scenarioUUID")
        }
    }

    fun getScenarioContent(scenarioId: String): ScenarioDto.ResponseScenarioDto? {
        val fileList = getFileList()
        var filePath = ""

        for (file in fileList) {
            if (file.scenarioId == scenarioId) {
                filePath = when (file.status) {
                    ScenarioStatus.READY -> "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.source}/${file.scenarioId}.json"
                    ScenarioStatus.PROGRESS -> "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.progress}/${file.scenarioId}.json"
                    ScenarioStatus.COMPLETE -> "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.complete}/${file.scenarioId}.json"
                    else -> ""
                }
            }
        }

        val fileContent = File(filePath).readText(charset = Charsets.UTF_8)
        val mapper = jacksonObjectMapper()

        return try {
            mapper.readValue<ScenarioDto.ResponseScenarioDto>(fileContent)
        } catch (e: Exception) {
            logger.error("Failed to get JSON source file: ${e.message.toString()}, scenarioUUID: $scenarioId")
            null
        }
    }

    private fun getUUID(): String {
        val dataFormat = SimpleDateFormat("yyyyMMddHHmmssSSS")
        return dataFormat.format(Date()).toString()
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
            logger.error("Failed to save JSON source file, ScenarioUUID: $scenarioUUID")
        }
    }

//    private fun getHeader(agentType: UserAgent): List<ScenarioDto.HeaderField> {
//        val headers: MutableList<ScenarioDto.HeaderField> = mutableListOf()
//
//        when (agentType) {
//            UserAgent.CHROME_114 -> headers.add(
//                ScenarioDto.HeaderField(
//                    HttpHeaderSection.USER_AGENT.value,
//                    UserAgent.CHROME_114.value
//                )
//            )
//            UserAgent.FIREFOX_114 -> headers.add(
//                ScenarioDto.HeaderField(
//                    HttpHeaderSection.USER_AGENT.value,
//                    UserAgent.FIREFOX_114.value
//                )
//            )
//        }
//
//        headers.add(
//            ScenarioDto.HeaderField(
//                HttpHeaderSection.ACCEPT.value,
//                "application/json, text/plain, */*")
//        )
//
//        headers.add(
//            ScenarioDto.HeaderField(
//                HttpHeaderSection.CONTENT_TYPE.value,
//                "application/json")
//        )
//
//        return headers.toList()
//    }

//    private fun getAuthorization(headers: MutableList<ScenarioDto.HeaderField>, jwtToken: String?) {
//        if (!jwtToken.isNullOrEmpty()) {
//            headers.add(ScenarioDto.HeaderField(HttpHeaderSection.AUTHORIZATION.value, "bearer $jwtToken"))
//        }
//    }
}