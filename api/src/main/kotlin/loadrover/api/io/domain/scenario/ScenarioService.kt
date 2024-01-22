package loadrover.api.io.domain.scenario

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import loadrover.api.io.config.LoadroverConfig
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

@Service
class ScenarioService(
    private val loadroverConfig: LoadroverConfig
) {
    private val log = LoggerFactory.getLogger(ScenarioService::class.java)

    fun getFileList(): MutableSet<FileDto> {
        val sourceFileList = searchDirectory("source")
        val workFileIdList = searchDirectory("work").map { it.scenarioId }
        val progressFileIdList = searchDirectory("progress").map { it.scenarioId }
        val resultFileIdList = searchDirectory("result").map { it.scenarioId }

        for (sourceFile in sourceFileList) {
            if (sourceFile.scenarioId in workFileIdList) sourceFile.status = ScenarioStatus.READY
            if (sourceFile.scenarioId in progressFileIdList) sourceFile.status = ScenarioStatus.PROGRESS
            if (sourceFile.scenarioId in resultFileIdList) sourceFile.status = ScenarioStatus.COMPLETE
        }

        return sourceFileList
    }

    // 시나리오 생성
    fun createScenario(request: ScenarioDto.RequestScenarioDto) {
        val scenarioUUID = getUUID()
        val scenarioClass = loadroverConfig.output.classNamePrefix + scenarioUUID

        saveSourceFile(request, scenarioUUID, scenarioClass)

        // Scheduler 이용
//        val fileName = file.originalFilename!!
//        val scenarioUUID = fileName.removePrefix(loadroverConfig.output.classNamePrefix).removeSuffix(".json")
//        val scenarioClass = fileName.removeSuffix(".json")
//        val request = jacksonObjectMapper().readValue<ScenarioDto.RequestScenarioDto>(file.bytes)

        val userAgent = UserAgent.CHROME_114
        val host = request.target.host
        var headerIdx = 0

        val codes = StringBuilder()
        codes.append("import java.time.Duration;\n")
        codes.append("import java.util.*;\n")
        codes.append("import io.gatling.javaapi.core.*;\n")
        codes.append("import io.gatling.javaapi.http.*;\n")
        codes.append("import io.gatling.javaapi.jdbc.*;\n")
        codes.append("import static io.gatling.javaapi.core.CoreDsl.*;\n")
        codes.append("import static io.gatling.javaapi.http.HttpDsl.*;\n")
        codes.append("import static io.gatling.javaapi.jdbc.JdbcDsl.*;\n")
        codes.append("public class $scenarioClass extends Simulation {{\n")
        codes.append("HttpProtocolBuilder httpProtocol = http\n")
        codes.append("    .baseUrl(\"${host}\")\n")
        codes.append("    .inferHtmlResources()\n")
        codes.append("    .acceptEncodingHeader(\"gzip, deflate, br\")\n")
        codes.append("    .acceptLanguageHeader(\"ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\")\n")
        codes.append("    .userAgentHeader(\"${userAgent.value}\");")

        val headerId = "headers_${headerIdx}"
        codes.append("Map<CharSequence, String> $headerId = new HashMap<>();\n")

        val headers: List<ScenarioDto.HeaderField> = getHeader(host, userAgent, request.auth.policy)

        for (headerField in headers) {
            codes.append("$headerId.put(\"$scenarioUUID\");\n")
        }
        // TODO: 이 부분 다시 체크
        headerIdx ++

        // Queue로 시나리오 순서 관리
        val orderBookQueue: Queue<String> = LinkedList(request.schedule.orderBook)
        val scenarioCtrl = ScenarioDto.ScenarioControl()
        codes.append("ScenarioBuilder scn = scenario(\"$scenarioUUID\")\n")

        var itm: String
        for (idx in 0 until  orderBookQueue.size) {
            // 포인터가 가르키는 원소를 리턴하거나 비었을 경우, null을 뱉어낸다.
            itm = orderBookQueue.poll()

            when (itm) {
                ScenarioAction.GetRequest.value -> {
                    codes.append(".exec(")
                    codes.append("http(\"request_${idx}__${request.schedule.getRequest[scenarioCtrl.idxGetRequest]?.endpoint}\")")
                    codes.append(".get(\"${request.schedule.getRequest[scenarioCtrl.idxGetRequest]?.endpoint}\")")
                    codes.append(".headers($headerId)")
                    codes.append(")\n")
                    scenarioCtrl.tickGetRequest()
                }
                ScenarioAction.Pause.value -> {
                    codes.append(".pause(${request.schedule.pause[scenarioCtrl.idxPause]?.sec})\n")
                    scenarioCtrl.tickPause()
                }
                ScenarioAction.PostRequest.value -> {
                    val payLoad: String? = request.schedule.postRequest[scenarioCtrl.idxPostRequest]?.payload?.replace("\"", "\\\"")

                    codes.append(".exec(")
                    codes.append(("http(\"request_${idx}__${request.schedule.postRequest[scenarioCtrl.idxPostRequest]?.endpoint}\")"))
                    codes.append(".post(\"${request.schedule.postRequest[scenarioCtrl.idxPostRequest]?.endpoint}\")")
                    codes.append(".headers($headerId)")
                    codes.append(".body(StringBody(\"${payLoad}\"))")
                    codes.append(")\n")
                    scenarioCtrl.tickPostRequest()
                }
            }

            codes.append(";\n")
            codes.append("setUp(scn.injectOpen(atOnceUsers(${request.scenario.concurrent}))).protocols(httpsProtocol);")
            codes.append("}}\n")
        }

        // work 디렉토리로 저장
        val savePath = "${loadroverConfig.gatling.path}/user_files/${loadroverConfig.gatling.work}/${scenarioClass}.java"

        try {
            val codeString: String = codes.toString()
            File(savePath).bufferedWriter().use {
                it.write(codeString)
            }
        } catch (e: Exception) {
            log.debug("ProjectUUID: $scenarioUUID")
        }
    }

    fun moveWorkToProgress(fileName: String) {
        val workDirectory = "${loadroverConfig.gatling.path}/user_files/${loadroverConfig.gatling.work}/$fileName"
        val progressDirectory = "${loadroverConfig.gatling.path}/user_files/${loadroverConfig.gatling.progress}/"

        val srcPath: Path = Path.of(workDirectory)
        val destinationDirectory: Path = Path.of(progressDirectory)

        try {
            Files.move(
                srcPath,
                destinationDirectory.resolve(srcPath.fileName),
                StandardCopyOption.REPLACE_EXISTING
            )

        } catch (e: Exception) {
            println("Failed to move progress directory: ${e.message}")
        }
    }

    private fun getUUID(): String {
        return System.currentTimeMillis().toString()
    }

    private fun getHeader(host: String, agentType: UserAgent, jwtToken: String?): List<ScenarioDto.HeaderField> {
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

        headers.add(ScenarioDto.HeaderField(HttpHeaderSection.ACCEPT.value, "application/json, text/plain, */*"))

        if (!jwtToken.isNullOrEmpty()) {
            headers.add(ScenarioDto.HeaderField(HttpHeaderSection.AUTHORIZATION.value, "bearer $jwtToken"))
        }

        return headers.toList()
    }

    private fun searchDirectory(path: String): MutableSet<FileDto> {
        val sourceFileDirectory = "${loadroverConfig.gatling.path}/user_files/${path}/"
        val directoryPath: Path = Path.of(sourceFileDirectory)
        val fileList: MutableSet<FileDto> = mutableSetOf()

        try {
            Files.walkFileTree(
                directoryPath,
                setOf(FileVisitOption.FOLLOW_LINKS),
                Integer.MAX_VALUE,
                object: SimpleFileVisitor<Path>() {
                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        fileList.plusAssign(
                            FileDto(
                                scenarioId = when (path) {
                                    loadroverConfig.gatling.source -> file?.fileName.toString().removeSuffix(".json")
//                                    loadroverConfig.gatling.result -> file?.fileName.toString().removeSuffix("")
                                    else -> file?.fileName.toString().removeSuffix(".java")
                                },
                                status = when (path) {
                                    loadroverConfig.gatling.source -> ScenarioStatus.PRECONVERSION
                                    loadroverConfig.gatling.work -> ScenarioStatus.READY
                                    loadroverConfig.gatling.progress -> ScenarioStatus.PROGRESS
                                    loadroverConfig.gatling.result -> ScenarioStatus.COMPLETE
                                    else -> ScenarioStatus.STOP
                                }
                            )
                        )
                        println("File Name: ${file?.fileName}, Path: $file")
                        return FileVisitResult.CONTINUE
                    }
                }
            )
        } catch (e: Exception) {
            println("Failed to read files: ${e.message}")
        }

        return fileList
    }

    // data 디렉토리 내 json 파일로 저장
    private fun saveSourceFile(request: ScenarioDto.RequestScenarioDto, scenarioUUID: String, scenarioClass: String) {
        val savePath = "${loadroverConfig.gatling.path}/user_files/${loadroverConfig.gatling.source}/${scenarioClass}.json"
        val serializedObject = jacksonObjectMapper().writeValueAsString(request)

        try {
            File(savePath).bufferedWriter().use {
                it.write(serializedObject)
            }
        } catch (e: Exception) {
            log.error("Failed to save JSON source file, ProjectUUID: $scenarioUUID")
        }
    }


}