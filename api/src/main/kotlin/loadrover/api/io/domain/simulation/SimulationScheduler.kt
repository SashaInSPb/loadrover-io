package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.nio.file.Files
import java.nio.file.Path

@EnableScheduling
@SpringBootApplication
class SimulationScheduler(
    private val fileUtils: FileUtils,
    private val loadroverConfig: LoadroverConfig
) {
    private val log = LoggerFactory.getLogger(SimulationService::class.java)

    @Scheduled(cron = "1 0 0 ? * 7") // 매주 일요일 00:00:01
    fun moveProgressToResult(): String {
        val progressFileList = fileUtils.searchDirectory("progress")
        // 폴더이므로 파일 찾는 방식은 구분되어야 함
        val resultFileIdList = fileUtils.searchDirectory("static/result").map { it.scenarioId }

        for (progressFile in progressFileList) {
            if (progressFile.scenarioId in resultFileIdList) {
                val progressDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.progress}/${progressFile.scenarioId}.kt"
                val srcPath: Path = Path.of(progressDirectory)

                try {
                    Files.delete(srcPath)
                } catch (e: Exception) {
                    log.error("Failed to delete test complete file, scenarioId: ${progressFile.scenarioId}")
                }
            }
        }
        return "Success"
    }

}