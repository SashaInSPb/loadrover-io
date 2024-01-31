package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime

@EnableScheduling
@SpringBootApplication
class SimulationScheduler(
    private val fileUtils: FileUtils,
    private val loadroverConfig: LoadroverConfig
) {
    private val logger = LoggerFactory.getLogger(SimulationScheduler::class.java)

    @Scheduled(cron = "0 */1 * * * *") // 매 1분
    fun moveProgressToComplete() {
        val progressFileList = fileUtils.searchFiles("progress")
        val resultFileIdList = fileUtils.searchResultFolders().map { it.scenarioId.replace("-\\d+".toRegex(),"") }

        for (progressFile in progressFileList) {
            val scenarioId = progressFile.scenarioId

            if (resultFileIdList.contains(scenarioId)) {
                val progressDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.progress}/${scenarioId}.json"
                val completeDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.complete}/${scenarioId}.json"

                val progressPath: Path = Path.of(progressDirectory)
                val completePath: Path = Path.of(completeDirectory)

                try {
                    Files.move(
                        progressPath,
                        completePath,
                        StandardCopyOption.REPLACE_EXISTING
                    )
                } catch (e: Exception) {
                    logger.error("Failed to move file: ${e.message}")
                }
            }
        }
        logger.debug("Check simulation result: ${LocalDateTime.now()}")
    }

}