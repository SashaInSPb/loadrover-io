package work

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import java.lang.Exception
import org.slf4j.LoggerFactory

class test20240202115155861: Simulation() {
private val logger = LoggerFactory.getLogger(test20240202115155861::class.java)

val httpProtocol = http
    .baseUrl("https://api-dev.boracat.io/tms")
    .inferHtmlResources()
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")

override fun before() { logger.debug("-------------------------------------------- Scenario test20240202115155861 is about to start.------------------------------------------------------------------") }
override fun after() { logger.debug("-------------------------------------------- Scenario test20240202115155861 was completed.------------------------------------------------------------------") }
init {
var headers_0: Map<String, String> = mutableMapOf(
"accept" to "application/json, text/plain, */*",
"Content-Type" to "application/json"
)
val scn0 = scenario("test20240202115155861-0")
.exec(http("request_POST_0").post("/auth/authentication").headers(headers_0).header("Authorization", "bearer #{accessToken0}").body(StringBody("")))
.pause(0)
.exec(http("request_POST_0").post("/project/list").headers(headers_0).header("Authorization", "bearer #{accessToken0}").body(StringBody("{\"projectTitle\":\"\",\"projectStatus\":[],\"dateType\":\"CONTRACT_END_DATE\",\"startDateTime\":\"2022-01-31T05:40:44.893Z\",\"endDateTime\":\"2024-03-31T05:40:44.893Z\",\"projectManagerUserIdList\":[],\"projectAssignerUserIdList\":[],\"page\":1,\"size\":10,\"sort\":\"ASC\"}")))
.pause(1)
.exec(http("request_POST_0").post("/task/allList").headers(headers_0).header("Authorization", "bearer #{accessToken0}").body(StringBody("{\"keyword\":\"\",\"taskStatus\":[],\"taskType\":[],\"startLanguageList\":[],\"destinationLanguageList\":[],\"dateType\":\"END_DATE\",\"startDateTime\":\"2022-01-31T05:41:49.735Z\",\"endDateTime\":\"2024-01-31T05:41:49.735Z\",\"projectManagerUserIdList\":[],\"workUserIdList\":[]}")))
.pause(0)
.exec(http("request_GET_0").get("/languageTask/assignWorker").headers(headers_0).header("Authorization", "bearer #{accessToken0}"))
.pause(0)
val scn1 = scenario("test20240202115155861-1")
.exec(http("request_POST_1").post("/auth/authentication").headers(headers_0).header("Authorization", "bearer #{accessToken1}").body(StringBody("")))
.pause(0)
.exec(http("request_POST_1").post("/project/list").headers(headers_0).header("Authorization", "bearer #{accessToken1}").body(StringBody("{\"projectTitle\":\"\",\"projectStatus\":[],\"dateType\":\"CONTRACT_END_DATE\",\"startDateTime\":\"2022-01-31T05:40:44.893Z\",\"endDateTime\":\"2024-03-31T05:40:44.893Z\",\"projectManagerUserIdList\":[],\"projectAssignerUserIdList\":[],\"page\":1,\"size\":10,\"sort\":\"ASC\"}")))
.pause(1)
.exec(http("request_POST_1").post("/task/allList").headers(headers_0).header("Authorization", "bearer #{accessToken1}").body(StringBody("{\"keyword\":\"\",\"taskStatus\":[],\"taskType\":[],\"startLanguageList\":[],\"destinationLanguageList\":[],\"dateType\":\"END_DATE\",\"startDateTime\":\"2022-01-31T05:41:49.735Z\",\"endDateTime\":\"2024-01-31T05:41:49.735Z\",\"projectManagerUserIdList\":[],\"workUserIdList\":[]}")))
.pause(0)
.exec(http("request_GET_1").get("/languageTask/assignWorker").headers(headers_0).header("Authorization", "bearer #{accessToken1}"))
.pause(0)
try {
this.setUp(
scn0.injectOpen(atOnceUsers(2)).protocols(httpProtocol),
scn1.injectOpen(atOnceUsers(2)).protocols(httpProtocol),
)
} catch(e: Exception) {
logger.error("Error during scenario setup: ${e.message}")}
}
}
