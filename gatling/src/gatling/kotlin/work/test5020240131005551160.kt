package work

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import java.lang.Exception

class test5020240131005551160: Simulation() {
val httpProtocol = http
    .baseUrl("https://api-dev.boracat.io/tms")
    .inferHtmlResources()
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")

override fun before() { println("-------------------------------------------- Scenario test5020240131005551160 is about to start.------------------------------------------------------------------") }
override fun after() { println("-------------------------------------------- Scenario test5020240131005551160 was completed.------------------------------------------------------------------") }
fun onError(errorMessage: String) { println("-------------------------------------------- An error occurred: $errorMessage ------------------------------------------------------------------") }

    init {
    var headers_0: Map<String, String> = mutableMapOf(
        "accept" to "application/json, text/plain, */*",
        "Content-Type" to "application/json"
    )

    val scn = scenario("test5020240131005551160")
    .exec(
        http("request_POST")
            .post("/auth/authentication")
            .headers(headers_0)
            .body(StringBody("{\"email\":\"taewan@2bytescorp.com\",\"password\":\"qwer1234!\",\"loginSite\":\"LPM\"}"))
            .check(jsonPath("$.accessToken").saveAs("accessToken")))
    .pause(1)
    .exec(
        http("request_GET")
            .get("/project/projectManagerList")
            .headers(headers_0)
            .header("Authorization", "bearer #{accessToken}")
    )
    .pause(1)
    .exec(http("request_GET").get("/languageTask/assignWorker").headers(headers_0).header("Authorization", "bearer #{accessToken}"))
    .pause(0)
    .exec(http("request_POST").post("/auth/authentication").headers(headers_0).body(StringBody("{\"email\":\"wonmoon.cha@2bytescorp.com\",\"password\":\"qwer1234!\",\"loginSite\":\"LPM\"}")).check(jsonPath("$.accessToken").saveAs("accessToken")))
    .pause(1)
    .exec(http("request_GET").get("/project/projectManagerList").headers(headers_0).header("Authorization", "bearer #{accessToken}"))
    .pause(1)
    .exec(http("request_GET").get("/languageTask/assignWorker").headers(headers_0).header("Authorization", "bearer #{accessToken}"))
    .pause(0)

        try {
            this.setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol)
        } catch (e: Exception) {
            this.onError("Error during scenario setup: ${e}")
        }
    }
}
