package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.domain.scenario.ScenarioDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

@Service
class SimulationService(
    private val loadroverConfig: LoadroverConfig
) {
    private val log = LoggerFactory.getLogger(SimulationService::class.java)

    fun moveProgressToResult() {
        val progressDirectory = "${loadroverConfig.gatling.path}/user_files/${loadroverConfig.gatling.progress}"
        val directoryPath: Path = Path.of(progressDirectory)

    try {
        Files.walkFileTree(
            directoryPath,
            setOf(FileVisitOption.FOLLOW_LINKS),
            Integer.MAX_VALUE,
            object: SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                    println("File Name: ${file?.fileName}, Path: $file")
                    return FileVisitResult.CONTINUE
                }
            }
        )
    } catch (e: Exception) {
        println("Error reading files: ${e.message}")
    }

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