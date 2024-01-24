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
                        val scenarioId = dir?.fileName
                            .toString()
                            .replace("-\\d+".toRegex(),"")

                        if (dir?.nameCount == resultPath.nameCount + 1) {
                            folderList.plusAssign(
                                FileDto(
                                    scenarioTitle = scenarioId.replace("\\d{17}$".toRegex(),""),
                                    scenarioId = scenarioId,
                                    status = ScenarioStatus.COMPLETE
                                )
                            )
//                            println("Directory Name: ${dir.fileName}, Path: $dir")
                        }
                        return FileVisitResult.CONTINUE
                    }

                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        // 파일은 출력하지 않고 지나갑니다.
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