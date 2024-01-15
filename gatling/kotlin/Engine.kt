import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

class Engine {
    
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val props: GatlingPropertiesBuilder = GatlingPropertiesBuilder()
                .resourcesDirectory(IDEPathHelper.gradleResourcesDirectory.toString())
                .resultsDirectory(IDEPathHelper.resultsDirectory.toString())
                .binariesDirectory(IDEPathHelper.gradleBinariesDirectory.toString())
                // 실행시킬 class package 디렉토리를 기입
                .simulationClass("TestSimulation")
            Gatling.fromMap(props.build())
        }
    }
}