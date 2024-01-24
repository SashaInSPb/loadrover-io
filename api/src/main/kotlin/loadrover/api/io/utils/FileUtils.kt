package loadrover.api.io.utils

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.domain.scenario.FileDto
import loadrover.api.io.domain.scenario.ScenarioStatus
import org.springframework.stereotype.Component
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

@Component
class FileUtils(
    private val loadroverConfig: LoadroverConfig
) {
    fun searchDirectory(path: String): MutableSet<FileDto> {
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
                        val regex = Regex("-[0-9]")

                        fileList.plusAssign(
                            FileDto(
                                scenarioTitle = when (path) {
                                    loadroverConfig.gatling.source -> fileName.removeSuffix(".json").replace(regex,"")
                                    else -> fileName.removeSuffix(".kt").replace(regex,"")
                                },
                                scenarioId = when (path) {
                                    loadroverConfig.gatling.source -> fileName.removeSuffix(".json")
                                    else -> fileName.removeSuffix(".kt")
                                },
                                status = when (path) {
                                    loadroverConfig.gatling.source -> ScenarioStatus.PRE_CONVERSION
                                    loadroverConfig.gatling.work -> ScenarioStatus.READY
                                    loadroverConfig.gatling.progress -> ScenarioStatus.PROGRESS
                                    loadroverConfig.gatling.result -> ScenarioStatus.COMPLETE
                                    else -> ScenarioStatus.STOP
                                }
                            )
                        )
                        println("File Name: ${file?.fileName}, Path: $file")
                        return FileVisitResult.CONTINUE
                    }
                }
            )
        } catch (e: Exception) {
            println("Failed to read files: ${e.message}")
        }

        return fileList
    }

    fun searchResultDirectory(): MutableSet<FileDto> {
        val fileDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.result}"
        val directoryPath: Path = Path.of(fileDirectory)
        val fileList: MutableSet<FileDto> = mutableSetOf()

        try {
            Files.walkFileTree(
                directoryPath,
                setOf(FileVisitOption.FOLLOW_LINKS),
                Int.MAX_VALUE,
                object : SimpleFileVisitor<Path>() {
                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        val fileName = file?.fileName.toString()

                        if (file?.nameCount == directoryPath.nameCount + 1) {
                            fileList.plusAssign(
                                FileDto(
                                    scenarioTitle = fileName.removeSuffix(".kt"),
                                    scenarioId = fileName.removeSuffix(".kt"),
                                    status = ScenarioStatus.COMPLETE
                                )
                            )
                        }
                        return FileVisitResult.CONTINUE
                    }
                }
            )
        } catch (e: Exception) {
            println("Failed to read files: ${e.message}")
        }
        return fileList
    }

}