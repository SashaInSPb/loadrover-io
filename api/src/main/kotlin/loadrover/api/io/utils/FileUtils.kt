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
                                scenarioTitle = fileName.removeSuffix(".json").replace("\\d{17}$".toRegex(),""),
                                scenarioId = fileName.removeSuffix(".json"),
                                status = when (path) {
                                    loadroverConfig.gatling.source -> ScenarioStatus.READY
                                    loadroverConfig.gatling.progress -> ScenarioStatus.PROGRESS
                                    loadroverConfig.gatling.complete -> ScenarioStatus.COMPLETE
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

    // 파일이 아닌 폴더로 결과물이 있는 result 출력용
    fun searchFolders(): MutableSet<FileDto> {
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
                                    scenarioId = simulationId
                                        .replace("-\\d+".toRegex(),""),
                                    status = ScenarioStatus.COMPLETE
                                )
                            )
//                            println("Directory Name: ${dir.fileName}, Path: $dir")
                        }
                        return FileVisitResult.CONTINUE
                    }

                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        return FileVisitResult.CONTINUE
                    }
                }
            )
        } catch (e: Exception) {
            println("Failed to read files: ${e.message}")
        }
        return folderList
    }

}