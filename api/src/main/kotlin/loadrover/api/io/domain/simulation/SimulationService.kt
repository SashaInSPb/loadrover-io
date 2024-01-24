package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service
class SimulationService(
    private val fileUtils: FileUtils,
    private val loadroverConfig: LoadroverConfig
) {
    private val log = LoggerFactory.getLogger(SimulationService::class.java)

    fun getSimulationResult(scenarioId: String): SimulationDto.ResultResponse {
        // 중복된 scenarioID가 있는 경우?



        return SimulationDto.ResultResponse(
            htmlPath = "",
            filePath = ""
        )
    }

    fun moveProgressToResult(): String {
        val progressFileList = fileUtils.searchDirectory("progress")
        // 폴더이므로 파일 찾는 방식은 구분되어야 함
        val resultFileIdList = fileUtils.searchResultDirectory().map { it.scenarioId }

        for (progressFile in progressFileList) {
            if (progressFile.scenarioId in resultFileIdList) {
                val progressDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.progress}/${progressFile.scenarioId}.kt"
                val srcPath: Path = Path.of(progressDirectory)

                try {
                    Files.delete(srcPath)
                } catch (e: Exception) {
                    log.error("Failed to delete test complete file: ${e.message}, scenarioId: ${progressFile.scenarioId}")
                }
            }
        }
        return "Success"
    }

    fun moveWorkToProgress(scenarioId: String) {
        val workDirectory = "${loadroverConfig.gatling.workPath}/${loadroverConfig.gatling.work}/${scenarioId}.kt"
        val progressDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.progress}/${scenarioId}.kt"

        val srcPath: Path = Path.of(workDirectory)
        val destinationDirectory: Path = Path.of(progressDirectory)

        try {
            Files.move(
                srcPath,
                destinationDirectory,
                StandardCopyOption.REPLACE_EXISTING
            )
        } catch (e: Exception) {
            log.error("Failed to move file: ${e.message}")
        }
    }


}