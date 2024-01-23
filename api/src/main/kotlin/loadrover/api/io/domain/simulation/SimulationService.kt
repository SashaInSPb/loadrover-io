package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.nio.file.*

@Service
class SimulationService(
    private val fileUtils: FileUtils,
    private val loadroverConfig: LoadroverConfig
) {
    private val log = LoggerFactory.getLogger(SimulationService::class.java)

    // 스케쥴러
    fun moveProgressToResult(): String {
        val progressFileList = fileUtils.searchDirectory("progress")
        // 폴더이므로 파일 찾는 방식은 구분되어야 함
        val resultFileIdList = fileUtils.searchDirectory("result").map { it.scenarioId }

        for (progressFile in progressFileList) {
            if (progressFile.scenarioId in resultFileIdList) {
                val progressDirectory = "${loadroverConfig.gatling.path}/user_files/${loadroverConfig.gatling.progress}/${progressFile.scenarioId}.kt"
                val srcPath: Path = Path.of(progressDirectory)

                try {
                    Files.delete(srcPath)
                } catch (e: Exception) {
                    log.error("Failed to delete process file, scenarioId: ${progressFile.scenarioId}")
                }
            }
        }
        return "Success"
    }


    fun getSimulationResult(scenarioId: String): SimulationDto.ResultResponse {
        // 같은 scenarioID가 있는 경우?
        //

        return SimulationDto.ResultResponse(
            htmlPath = "",
            filePath = ""
        )
    }


}