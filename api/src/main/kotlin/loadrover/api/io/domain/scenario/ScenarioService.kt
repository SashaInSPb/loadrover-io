package loadrover.api.io.domain.scenario

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

    // 시나리오 생성
    fun createScenario(request: ScenarioDto.RequestProjectDto) {
        val projectUUID = getUUID()
        val scenarioCtrl = ScenarioDto.ScenarioControl()
        val projectClass = loadroverConfig.output.classNamePrefix + projectUUID
        val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
        val host = "https://dev.boracat.io"


        // header field 값 추가
        val codes = StringBuilder()
        codes.append("import java.time.Duration;\n")
        codes.append("import java.util.*;\n")
        codes.append("import io.gatling.javaapi.core.*;\n")
        codes.append("import io.gatling.javaapi.http.*;\n")
        codes.append("import io.gatling.javaapi.jdbc.*;\n")
        codes.append("import static io.gatling.javaapi.core.CoreDsl.*;\n")
        codes.append("import static io.gatling.javaapi.http.HttpDsl.*;\n")
        codes.append("import static io.gatling.javaapi.jdbc.JdbcDsl.*;\n")
        codes.append("public class $projectClass extends Simulation {{\n")
        codes.append("HttpProtocolBuilder httpProtocol = http\n")
        codes.append("    .baseUrl(\"${host}\")\n")
        codes.append("    .inferHtmlResources()\n")
        codes.append("    .acceptEncodingHeader(\"gzip, deflate, br\")\n")
        codes.append("    .acceptLanguageHeader(\"ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\")\n")
        codes.append("    .userAgentHeader(\"${userAgent}\");")

        val headerId = "headers_0"
        codes.append("Map<CharSequence, String> $headerId = new HashMap<>();\n")

        val headers: List<ScenarioDto.HeaderField> = getHeader(host, UserAgent.CHROME_114, request.auth.policy)

        for (headerField in headers) {
            codes.append("$headerId.put(\"$projectUUID\");\n")
        }

        // Queue로 시나리오 순서 관리
        var itm: String
        val orderBookQueue: Queue<String> = LinkedList(request.schedule.orderBook)
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
            codes.append("setUp(scn.injectOpen(atOnceUsers(${request.project.concurrent}))).protocols(httpsProtocol);")
            codes.append("}}\n")
        }

        val savePath = "${loadroverConfig.gatling.path}/user-files/${loadroverConfig.gatling.simulation}/${projectClass}.java"


        try {
            val codeString: String = codes.toString()
            File(savePath).bufferedWriter().use { it.write(codeString) }
        } catch (e: Exception) {
            log.debug("ProjectUUID: $projectUUID")
        }

    }

    private fun getUUID(): String {
        return UUID.randomUUID().toString()
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

    fun moveSimulationToProgress() {
        val sourceFile = "${loadroverConfig.gatling.path}/user_files/${loadroverConfig.gatling.simulation}/SimulationDev8c9f8b9c-1a66-4367-ad02-15060a3407dc.java"
        val progressDirectory = "${loadroverConfig.gatling.path}/user_files/${loadroverConfig.gatling.simulation}/progress/"

        val srcPath: Path = Path.of(sourceFile)
        val destinationDirectory: Path = Path.of(progressDirectory)

        try {
            Files.move(
                srcPath,
                destinationDirectory.resolve(srcPath.fileName),
                StandardCopyOption.REPLACE_EXISTING
            )

            println("File moved successfully!")
        } catch (e: Exception) {
            println("Error moving file: ${e.message}")
        }
    }

    fun getScenarioList() {
        val progressDirectory = "${loadroverConfig.gatling.path}/user_files/${loadroverConfig.gatling.simulation}/progress/"
        val directoryPath: Path = Path.of(progressDirectory)

        try {
            Files.walkFileTree(
                directoryPath,
                setOf(FileVisitOption.FOLLOW_LINKS),
                Integer.MAX_VALUE,
                object: SimpleFileVisitor<Path>() {
                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        println("File Name: ${file?.fileName}, Path: $file")
                        return FileVisitResult.CONTINUE
                    }
                }
            )
        } catch (e: Exception) {
            println("Error reading files: ${e.message}")
        }
    }

}