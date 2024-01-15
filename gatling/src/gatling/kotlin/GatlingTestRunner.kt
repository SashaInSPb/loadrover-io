import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

class GatlingTestRunner(
    private val simulationClass: String
) {
    fun runTest() {
        val props: GatlingPropertiesBuilder = GatlingPropertiesBuilder()
            .resourcesDirectory(IDEPathHelper.gradleResourcesDirectory.toString())
            .resultsDirectory(IDEPathHelper.resultsDirectory.toString())
            .binariesDirectory(IDEPathHelper.gradleBinariesDirectory.toString())
            // 실행시킬 class package 디렉토리를 기입
            .simulationClass("TestSimulation")
        Gatling.fromMap(props.build())
    }
}

fun main(args: Array<String>) {
    // 메인 메서드에서 사용할 GatlingTestRunner 인스턴스 생성
    val runner = GatlingTestRunner("TestSimulation")

    // 원하는 동작을 호출
    runner.runTest()
}