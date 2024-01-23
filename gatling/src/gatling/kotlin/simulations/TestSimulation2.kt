package simulations

import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import java.time.Duration

class TestSimulation2: Simulation() {
    val httpProtocol = http
        .baseUrl("http://localhost:8089/tms")
        .acceptHeader("application/json")

    val scn = scenario("TestSimulation2")
        .exec(http("request1").get("/health-check2"))

    init {
        this.setUp(scn.injectOpen(constantUsersPerSec(50.0).during(Duration.ofSeconds(15))))
            .protocols(httpProtocol)
    }
}

