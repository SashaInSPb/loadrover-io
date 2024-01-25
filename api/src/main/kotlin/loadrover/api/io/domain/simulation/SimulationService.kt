package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
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
        // result 폴더에서 scenarioId를 가지고 있는 폴더로 접근
        val resultList = fileUtils.searchFolders()
        var htmlPath = ""

        for (result in resultList) {
            if (result.scenarioId.contains(scenarioId)) {
                val resultDirectory = "${loadroverConfig.gatling.result}/${result.scenarioId}/index.html"
                htmlPath = ServletUriComponentsBuilder.fromCurrentContextPath().path(resultDirectory).toUriString()
            }
        }

        return SimulationDto.ResultResponse(
            htmlPath = htmlPath
        )
    }

    fun moveProgressToResult(): String {
        val progressFileList = fileUtils.searchFiles("progress")
        // 폴더이므로 파일 찾는 방식은 구분되어야 함
        val resultFileIdList = fileUtils.searchFolders().map { it.scenarioId }

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

        val workPath: Path = Path.of(workDirectory)
        val progressPath: Path = Path.of(progressDirectory)

        try {
            Files.move(
                workPath,
                progressPath,
                StandardCopyOption.REPLACE_EXISTING
            )
        } catch (e: Exception) {
            log.error("Failed to move file: ${e.message}")
        }
    }


}