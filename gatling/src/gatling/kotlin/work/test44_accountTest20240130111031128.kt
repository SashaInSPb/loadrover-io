package work

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*

class test44_accountTest20240130111031128: Simulation() {
val httpProtocol = http
    .baseUrl("http://localhost:8089/tms")
    .inferHtmlResources()
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
val headers_0: MutableMap<CharSequence, String> = HashMap()
init {
headers_0.put("UserAgent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
headers_0.put("accept","application/json, text/plain, */*")
headers_0.put("Content-Type","application/json")
val scn = scenario("test44_accountTest+20240130111031128")
.exec(http("request_POST").post("/auth/authentication").headers(headers_0).body(StringBody("{\"email\":\"taewan@2bytescorp.com\",\"password\":\"qwer1234!\",\"loginSite\":\"LPM\"}")).check(jsonPath("$.accessToken").saveAs("accessToken")))
.pause(1)
.exec(http("request_GET").get("/health-check2").headers(headers_0))
.pause(1)
.exec(http("request_POST").post("/auth/authentication").headers(headers_0).body(StringBody("{\"email\":\"88parksw@2bytescorp.com\",\"password\":\"qwer1234!\",\"loginSite\":\"LPM\"}")).check(jsonPath("$.accessToken").saveAs("accessToken")))
.pause(1)
.exec(http("request_GET").get("/health-check2").headers(headers_0))
.pause(1)
.exec(http("request_POST").post("/auth/authentication").headers(headers_0).body(StringBody("{\"email\":\"88parksw2@2bytescorp.com\",\"password\":\"qwer1234!\",\"loginSite\":\"LPM\"}")).check(jsonPath("$.accessToken").saveAs("accessToken")))
.pause(1)
.exec(http("request_GET").get("/health-check2").headers(headers_0))
.pause(1)
this.setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol)
}}
