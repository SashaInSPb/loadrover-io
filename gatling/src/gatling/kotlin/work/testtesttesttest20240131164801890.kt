package work

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import java.lang.Exception
import org.slf4j.LoggerFactory

class testtesttesttest20240131164801890: Simulation() {
private val logger = LoggerFactory.getLogger(testtesttesttest20240131164801890::class.java)

val httpProtocol = http
    .baseUrl("https://api-dev.boracat.io/tms")
    .inferHtmlResources()
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")

override fun before() { logger.debug("-------------------------------------------- Scenario testtesttesttest20240131164801890 is about to start.------------------------------------------------------------------") }
override fun after() { logger.debug("-------------------------------------------- Scenario testtesttesttest20240131164801890 was completed.------------------------------------------------------------------") }
init {
var headers_0: Map<String, String> = mutableMapOf(
"accept" to "application/json, text/plain, */*",
"Content-Type" to "application/json"
)
val scn = scenario("testTesttestTest20240131164801890")
.exec(http("request_POST").post("/auth/authentication").headers(headers_0).body(StringBody("")))
.pause(0)
.exec(http("request_POST").post("/project/list").headers(headers_0).body(StringBody("{\"projectTitle\":\"\",\"projectStatus\":[],\"dateType\":\"CONTRACT_END_DATE(계약종료일)\",\"startDateTime\":\"2022-01-31T05:40:44.893Z\",\"endDateTime\":\"2024-03-31T05:40:44.893Z\",\"projectManagerUserIdList\":[],\"projectAssignerUserIdList\":[],\"page\":1,\"size\":10,\"sort\":\"ASC\"}")))
.pause(1)
.exec(http("request_POST").post("").headers(headers_0).body(StringBody("{\"keyword\":\"\",\"taskStatus\":[],\"taskType\":[],\"startLanguageList\":[],\"destinationLanguageList\":[],\"dateType\":\"END_DATE\",\"startDateTime\":\"2022-01-31T05:41:49.735Z\",\"endDateTime\":\"2024-01-31T05:41:49.735Z\",\"projectManagerUserIdList\":[],\"workUserIdList\":[]}")))
.pause(0)
.exec(http("request_GET").get("/languageTask/assignWorker").headers(headers_0))
.pause(0)
try {
this.setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol)
} catch(e: Exception) {
logger.error("Error during scenario setup: ${e.message}")}
}
}
