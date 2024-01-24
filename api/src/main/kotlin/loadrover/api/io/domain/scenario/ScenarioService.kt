package loadrover.api.io.domain.scenario

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.Date

@Service
class ScenarioService(
    private val loadroverConfig: LoadroverConfig,
    private val fileUtils: FileUtils
) {
    private val log = LoggerFactory.getLogger(ScenarioService::class.java)

    fun getFileList(): MutableSet<FileDto> {
        val sourceFileList = fileUtils.searchDirectory("source")
        val workFileIdList = fileUtils.searchDirectory("work").map { it.scenarioId }
        val progressFileIdList = fileUtils.searchDirectory("progress").map { it.scenarioId }
        val resultFileIdList = fileUtils.searchDirectory("static/result").map { it.scenarioId }

        for (sourceFile in sourceFileList) {
            if (sourceFile.scenarioId in workFileIdList) sourceFile.status = ScenarioStatus.READY
            if (sourceFile.scenarioId in progressFileIdList) sourceFile.status = ScenarioStatus.PROGRESS
            if (sourceFile.scenarioId in resultFileIdList) sourceFile.status = ScenarioStatus.COMPLETE
        }

        return sourceFileList
    }

    fun createScenario(request: ScenarioDto.RequestScenarioDto) {
        val scenarioUUID = getUUID()
        val scenarioClass = loadroverConfig.output.classNamePrefix + scenarioUUID

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
        codes.append("val $headerId: MutableMap<CharSequence, String> = HashMap()\n")
        val headers: List<ScenarioDto.HeaderField> = getHeader(userAgent, request.task.jwtObjectName)

        codes.append("init {\n")
        for (headerField in headers) {
            codes.append("$headerId.put(\"${headerField.section}\",\"${headerField.value}\")\n")
        }
        codes.append("val scn = scenario(\"$scenarioUUID\")\n")
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
                    val payload = action.value.params.replace("\"", "\\\"")

                    codes.append(".exec(")
                    codes.append(("http(\"request_${action.value.apiType}\")"))
                    codes.append(".post(\"${action.value.apiUrl}\")")
                    codes.append(".headers($headerId)")
                    codes.append(".body(StringBody(\"${payload}\"))")
                    codes.append(")\n")
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
        codes.append("this.setUp(scn.injectOpen(atOnceUsers(${request.task.concurrent}))).protocols(httpProtocol)")
        codes.append("}}\n")

        //TODO: work 따로 디렉토리 관리
        val savePath = "gatling/src/gatling/kotlin/work/${scenarioClass}.kt"

        try {
            val codeString: String = codes.toString()
            File(savePath).bufferedWriter().use {
                it.write(codeString)
            }
        } catch (e: Exception) {
            log.error("Failed to create: ${e.message}, scenarioUUID: $scenarioUUID")
        }
    }

    fun moveWorkToProgress(fileName: String) {
        val workDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.work}/$fileName"
        val progressDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.progress}/"

        val srcPath: Path = Path.of(workDirectory)
        val destinationDirectory: Path = Path.of(progressDirectory)

        try {
            Files.move(
                srcPath,
                destinationDirectory.resolve(srcPath.fileName),
                StandardCopyOption.REPLACE_EXISTING
            )

        } catch (e: Exception) {
            log.error("Failed to move progress directory: ${e.message}")
        }
    }

    private fun getUUID(): String {
        val dataFormat = SimpleDateFormat("yyyyMMddHHmmssSSS")
        return dataFormat.format(Date()).toString()
    }

    private fun getHeader(agentType: UserAgent, jwtToken: String?): List<ScenarioDto.HeaderField> {
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

        if (!jwtToken.isNullOrEmpty()) {
            headers.add(ScenarioDto.HeaderField(HttpHeaderSection.AUTHORIZATION.value, "bearer $jwtToken"))
        }

        return headers.toList()
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