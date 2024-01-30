package work

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import java.lang.Exception

class test5320240131012101810: Simulation() {
val httpProtocol = http
    .baseUrl("https://api-dev.boracat.io/tms")
    .inferHtmlResources()
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
val headers_0: Map<String, String> = mutableMapOf()

override fun before() { println("-------------------------------------------- Scenario test5320240131012101810 is about to start.------------------------------------------------------------------") }
override fun after() { println("-------------------------------------------- Scenario test5320240131012101810 was completed.------------------------------------------------------------------") }
fun onError(errorMessage: String) { println("-------------------------------------------- An error occurred: $errorMessage ------------------------------------------------------------------") }
init {
var headers_0: Map<String, String> = mutableMapOf(
"accept" to "application/json, text/plain, */*",
"Content-Type" to "application/json"
)
val scn = scenario("test5320240131012101810")
.exec(http("request_POST").post("/auth/authentication").headers(headers_0).body(StringBody("")))
.pause(1)
.exec(http("request_GET").get("/project/projectManagerList").headers(headers_0))
.pause(1)
.exec(http("request_GET").get("/languageTask/assignWorker").headers(headers_0))
.pause(0)
try {
this.setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol)
} catch(e: Exception) {
this.onError("Error during scenario setup: ${e.message}")}
}
}
