package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@EnableScheduling
@SpringBootApplication
class SimulationScheduler(
    private val fileUtils: FileUtils,
    private val loadroverConfig: LoadroverConfig
) {
    private val logger = LoggerFactory.getLogger(SimulationScheduler::class.java)

    @Scheduled(cron = "0 */1 * * * *") // 매 1분으로 설정
    fun moveProgressToComplete() {
        val progressFileList = fileUtils.searchFiles("progress")
        val resultFileIdList = fileUtils.searchResultFolders().map { it.scenarioId.replace("-\\d+".toRegex(),"") }

        for (progressFile in progressFileList) {
            val scenarioId = progressFile.scenarioId

            if (resultFileIdList.contains(scenarioId)) {
                fileUtils.moveJsonFile(scenarioId, loadroverConfig.gatling.progress, loadroverConfig.gatling.complete)
            }
        }
        logger.debug("Check simulation result: {}", LocalDateTime.now())
    }

}