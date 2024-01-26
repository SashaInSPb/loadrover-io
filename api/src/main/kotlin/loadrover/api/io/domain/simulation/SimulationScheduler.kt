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
    private val log = LoggerFactory.getLogger(SimulationService::class.java)

    @Scheduled(cron = "0 */1 * * * *") // 매 1분
    fun moveProgressToComplete() {
        val progressFileList = fileUtils.searchFiles("progress")
        val resultFileIdList = fileUtils.searchFolders().map { it.scenarioId }

        for (progressFile in progressFileList) {
            val scenarioId = progressFile.scenarioId

            if (progressFile.scenarioId in resultFileIdList) {
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
                    log.error("Failed to move file: ${e.message}")
                }
            }
        }
        println("Check simulation results: ${LocalDateTime.now()}")
    }

}