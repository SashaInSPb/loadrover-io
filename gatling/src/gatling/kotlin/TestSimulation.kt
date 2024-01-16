//import io.gatling.core.scenario.Simulation
//import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
//import io.gatling.javaapi.core.CoreDsl.scenario
//import io.gatling.javaapi.core.Simulation
//import io.gatling.javaapi.http.HttpDsl.http
//import java.time.Duration
//
//class TestSimulation: Simulation() {
//    val httpProtocol = http
//        .baseUrl("http://localhost:8080")
//        .acceptHeader("application/json")
//
//    val scn = scenario("TestSimulation")
//        .exec(http("request1").get("/health-check"))
//
//    init {
//        this.setUp(scn.injectOpen(constantUsersPerSec(50.0).during(Duration.ofSeconds(15))))
//            .protocols(httpProtocol)
//    }
//}

