package work

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import java.lang.Exception

class test4620240130180232936: Simulation() {
val httpProtocol = http
    .baseUrl("https://api-dev.boracat.io/tms")
    .inferHtmlResources()
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
//    .authorizationHeader("bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRoIjoiIiwic3ViIjoiNjAiLCJpYXQiOjE3MDY2MjAyNzksImV4cCI6MTcwNjcwNjY3OSwibmJmIjoxNzA2NjIwMjc5LCJhdWQiOiJib3JhLmF1ZGllbmNlLmNvbW1vbi5kZXYiLCJpc3MiOiJib3JhLmF1dGguZGV2In0.8uDQysn4-CSp5eCr_iyqFdcu57DEOS4d1Of6Dewv2FY")
val headers_0: MutableMap<CharSequence, String> = HashMap()

override fun before() {
    println("-------------------------------------------- Scenario is about to start.------------------------------------------------------------------ ")
}

// 시나리오 실행 후에 호출되는 메서드
override fun after() {
    println("-------------------------------------------- Scenario completed.------------------------------------------------------------------ ")
}

fun onError(errorMessage: String) {
    println("An error occurred: $errorMessage")
}

// 예외 처리를 위한 메서드
//fun onError(errorMessage: String) {
//    println("An error occurred: $errorMessage")
//}

init {
    var headers_0: Map<String, String> = mutableMapOf(
        "UserAgent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",
        "accept" to "application/json, text/plain, */*",
        "Content-Type" to "application/json"
    )

    val scn = scenario("test4620240130180232936")
    .exec(http("request_POST").post("/auth/authentication").headers(headers_0).body(StringBody("{\"email\":\"taewan@2bytescorp.com\",\"password\":\"qwer1234!\",\"loginSite\":\"LPM\"}"))
    .check(jsonPath("$.accessToken").saveAs("accessToken")))
    .pause(1)
    .exec(
        http("프로젝트매니저리스트조회")
            .get("/project/projectManagerList")
            .headers(headers_0)
            .header("Authorization", "bearer #{accessToken}")
    )
    .pause(1)
    .exec(
        http("작업담당자조회")
            .get("/languageTask/assignWorker")
            .headers(headers_0)
            .header("Authorization", "bearer #{accessToken}")
    )
    .pause(0)
    try {
        this.setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol)
    } catch (e: Exception) {
        this.onError("Error during scenario setup: ${e.message}")
    }
}
}
