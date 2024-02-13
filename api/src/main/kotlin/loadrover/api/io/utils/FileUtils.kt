package loadrover.api.io.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.domain.scenario.FileDto
import loadrover.api.io.domain.scenario.ScenarioDto
import loadrover.api.io.domain.scenario.ScenarioService
import loadrover.api.io.domain.scenario.ScenarioStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

@Component
class FileUtils(
    private val loadroverConfig: LoadroverConfig
) {
    private val logger = LoggerFactory.getLogger(ScenarioService::class.java)

    fun searchFiles(path: String): MutableSet<FileDto> {
        val fileDirectory = if (path == "work") "${loadroverConfig.gatling.workPath}/${path}" else "${loadroverConfig.gatling.path}/${path}"
        val directoryPath: Path = Path.of(fileDirectory)
        val fileList: MutableSet<FileDto> = mutableSetOf()

        try {
            Files.walkFileTree(
                directoryPath,
                setOf(FileVisitOption.FOLLOW_LINKS),
                Integer.MAX_VALUE,
                object: SimpleFileVisitor<Path>() {
                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        val fileName = file?.fileName.toString()

                        fileList.plusAssign(
                            FileDto(
                                scenarioTitle = if (path == "work") fileName.removeSuffix(".kt") else fileName.removeSuffix(".json").replace("\\d{17}$".toRegex(),""),
                                scenarioId = if (path == "work") fileName.removeSuffix(".kt") else fileName.removeSuffix(".json"),
                                status = when (path) {
                                    loadroverConfig.gatling.source -> ScenarioStatus.READY
                                    loadroverConfig.gatling.progress -> ScenarioStatus.PROGRESS
                                    loadroverConfig.gatling.complete -> ScenarioStatus.COMPLETE
                                    else -> ScenarioStatus.STOP
                                }
                            )
                        )
                        return FileVisitResult.CONTINUE
                    }
                }
            )
        } catch (e: Exception) {
            println("Failed to read files: ${e.message.toString()}")
        }

        return fileList
    }

    // 파일이 아닌 폴더로 결과물이 있는 result 출력용
    fun searchResultFolders(): MutableSet<FileDto> {
        val resultDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.result}"
        val resultPath: Path = Path.of(resultDirectory)
        val folderList: MutableSet<FileDto> = mutableSetOf()

        try {
            Files.walkFileTree(
                resultPath,
                setOf(FileVisitOption.FOLLOW_LINKS),
                Int.MAX_VALUE,
                object : SimpleFileVisitor<Path>() {
                    override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        val simulationId = dir?.fileName.toString()

                        if (dir?.nameCount == resultPath.nameCount + 1) {
                            folderList.plusAssign(
                                FileDto(
                                    scenarioTitle = simulationId
                                        .replace("-\\d+".toRegex(),"")
                                        .replace("\\d{17}$".toRegex(),""),
                                    scenarioId = simulationId,
                                    status = ScenarioStatus.COMPLETE
                                )
                            )

                        }
                        return FileVisitResult.CONTINUE
                    }

                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        return FileVisitResult.CONTINUE
                    }
                }
            )
        } catch (e: Exception) {
            println("Failed to read files: ${e.message.toString()}")
        }
        return folderList
    }

    fun deleteFile(directory: String) {
        try {
            File(directory).deleteRecursively()
        } catch (e: Exception) {
            logger.error("Failed to delete file: ${e.message.toString()}")
        }
    }

    fun moveJsonFile(scenarioId: String, startFolder: String, destinationFolder: String) {
        val startDirectory = "${loadroverConfig.gatling.path}/${startFolder}/${scenarioId}.json"
        val destinationDirectory = "${loadroverConfig.gatling.path}/${destinationFolder}/${scenarioId}.json"

        val startPath: Path = Path.of(startDirectory)
        val destinationPath: Path = Path.of(destinationDirectory)

        try {
            Files.move(
                startPath,
                destinationPath,
                StandardCopyOption.REPLACE_EXISTING
            )
        } catch (e: Exception) {
            logger.error("Failed to move JSON file: ${e.message.toString()}, ScenarioUUID: $scenarioId")
        }
    }

    fun saveJsonFile(request: ScenarioDto.ScenarioCreateDto, scenarioUUID: String, scenarioClass: String) {
        val savePath = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.source}/${scenarioClass}.json"
        val serializedObject = jacksonObjectMapper().writeValueAsString(request)

        try {
            File(savePath).bufferedWriter().use {
                it.write(serializedObject)
            }
        } catch (e: Exception) {
            logger.error("Failed to save JSON file: ${e.message.toString()}, ScenarioUUID: $scenarioUUID")
        }
    }

    // 덮어쓰기 메서드 추가
    fun reviseJsonFile(request: ScenarioDto.ScenarioReviseDto) {
        val savePath = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.source}/${request.scenarioId}.json"
        val serializedObject = jacksonObjectMapper().writeValueAsString(request.removeScenarioId())

        try {
            BufferedWriter(FileWriter(savePath)).use {
                it.write(serializedObject)
            }
        } catch (e: Exception) {
            logger.error("Failed to save revised JSON file: ${e.message.toString()}, ScenarioUUID: ${request.scenarioId}")
        }
    }

}