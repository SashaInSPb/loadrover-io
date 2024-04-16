package loadrover.api.io.utils

import loadrover.api.io.config.LoadroverProperties
import loadrover.api.io.domain.base.BaseDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.*
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Component
class FileUtils(
    private val loadroverConfig: LoadroverProperties
) {
    private val logger = LoggerFactory.getLogger(FileUtils::class.java)

    // 폴더이름 리스트 뽑기
    fun searchDirectories(directory: String): MutableSet<BaseDto.ResultDto> {
        val resultPath: Path = Path.of(directory)
        val folderList: MutableSet<BaseDto.ResultDto> = mutableSetOf()

        try {
            Files.walkFileTree(
                resultPath,
                setOf(FileVisitOption.FOLLOW_LINKS),
                Int.MAX_VALUE,
                object : SimpleFileVisitor<Path>() {
                    override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                        val fileName = dir?.fileName.toString()

                        if (dir?.nameCount == resultPath.nameCount + 1) {
                            folderList.plusAssign(
                                BaseDto.ResultDto(
                                    resultFolderName = fileName
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
            logger.error("Failed to read result directories: ${e.message.toString()}")
        }
        return folderList
    }

    //TODO: memory leak check
    fun zipAll(directory: String, zipFile: String) {
        val sourceFile = File(directory)

        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use {
            zipFiles(it, sourceFile, "")
        }
    }
    fun zipFiles(zipOut: ZipOutputStream, sourceFile: File, parentDirPath: String) {
        val data = ByteArray(2048)

        sourceFile.listFiles()?.forEach { f ->
            if (f.isDirectory) {
                val path = if (parentDirPath == "") f.name else parentDirPath + File.separator + f.name
                val entry = ZipEntry(path + File.separator)
                entry.time = f.lastModified()
                entry.isDirectory
                entry.size = f.length()
                zipFiles(zipOut, f, path)

            } else {
                FileInputStream(f).use { fi ->
                    BufferedInputStream(fi).use { origin ->
                        val path = parentDirPath + File.separator + f.name
                        val entry = ZipEntry(path)
                        entry.time = f.lastModified()
                        entry.isDirectory
                        entry.size = f.length()
                        zipOut.putNextEntry(entry)
                        while (true) {
                            val readBytes = origin.read(data)
                            if (readBytes == -1) break
                            zipOut.write(data, 0, readBytes)
                        }
                    }
                }
            }
        }
    }

    fun deleteDirectory(directory: String) {
        try {
            File(directory).deleteRecursively()
        } catch (e: Exception) {
            logger.error("Failed to delete file: ${e.message.toString()}")
        }
    }

//    fun searchFiles(path: String): MutableSet<FileDto> {
//        val fileDirectory = if (path == "work") "${loadroverConfig.gatling.workPath}/${path}" else "${loadroverConfig.gatling.path}/${path}"
//        val directoryPath: Path = Path.of(fileDirectory)
//        val fileList: MutableSet<FileDto> = mutableSetOf()
//
//        try {
//            Files.walkFileTree(
//                directoryPath,
//                setOf(FileVisitOption.FOLLOW_LINKS),
//                Integer.MAX_VALUE,
//                object: SimpleFileVisitor<Path>() {
//                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
//                        val uploadFileName = file?.uploadFileName.toString()
//
//                        fileList.plusAssign(
//                            FileDto(
//                                scenarioTitle = if (path == "work") uploadFileName.removeSuffix(".kt") else uploadFileName.removeSuffix(".json").replace("\\d{17}$".toRegex(),""),
//                                scenarioId = if (path == "work") uploadFileName.removeSuffix(".kt") else uploadFileName.removeSuffix(".json"),
//                                status = when (path) {
//                                    loadroverConfig.gatling.source -> ScenarioStatus.READY
//                                    loadroverConfig.gatling.progress -> ScenarioStatus.PROGRESS
//                                    loadroverConfig.gatling.complete -> ScenarioStatus.COMPLETE
//                                    else -> ScenarioStatus.STOP
//                                }
//                            )
//                        )
//                        return FileVisitResult.CONTINUE
//                    }
//                }
//            )
//        } catch (e: Exception) {
//            logger.error("Failed to read files: ${e.message.toString()}")
//        }
//
//        return fileList
//    }

//    fun searchHtmlFiles(simulationId: String): MutableSet<HtmlFileDto> {
//        val fileDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.result}/$simulationId"
//        val directoryPath: Path = Path.of(fileDirectory)
//        val fileList: MutableSet<HtmlFileDto> = mutableSetOf()
//
//        try {
//            Files.walkFileTree(
//                directoryPath,
//                setOf(FileVisitOption.FOLLOW_LINKS),
//                Integer.MAX_VALUE,
//                object: SimpleFileVisitor<Path>() {
//                    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
//                        val uploadFileName = file?.uploadFileName.toString()
//
//                        fileList.plusAssign(
//                            HtmlFileDto(
//                                uploadFileName = uploadFileName
//                            )
//                        )
//                        return FileVisitResult.CONTINUE
//                    }
//                }
//            )
//        } catch (e: Exception) {
//            logger.error("Failed to read html files: ${e.message.toString()}")
//        }
//
//        return fileList
//    }
//
//    fun moveJsonFile(scenarioId: String, startFolder: String, destinationFolder: String) {
//        val startDirectory = "${loadroverConfig.gatling.path}/${startFolder}/${scenarioId}.json"
//        val destinationDirectory = "${loadroverConfig.gatling.path}/${destinationFolder}/${scenarioId}.json"
//
//        val startPath: Path = Path.of(startDirectory)
//        val destinationPath: Path = Path.of(destinationDirectory)
//
//        try {
//            Files.move(
//                startPath,
//                destinationPath,
//                StandardCopyOption.REPLACE_EXISTING
//            )
//        } catch (e: Exception) {
//            logger.error("Failed to move JSON file: ${e.message.toString()}, ScenarioUUID: $scenarioId")
//        }
//    }
//
//    fun saveJsonFile(request: ScenarioDto.ScenarioCreateDto, scenarioUUID: String, scenarioClass: String) {
//        val savePath = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.source}/${scenarioClass}.json"
//        val serializedObject = jacksonObjectMapper().writeValueAsString(request)
//
//        try {
//            File(savePath).bufferedWriter().use {
//                it.write(serializedObject)
//            }
//        } catch (e: Exception) {
//            logger.error("Failed to save JSON file: ${e.message.toString()}, ScenarioUUID: $scenarioUUID")
//        }
//    }
//
//    // 덮어쓰기 메서드 추가
//    // 기존 json file 삭제 필요
//    fun reviseJsonFile(request: ScenarioDto.ScenarioReviseDto) {
//        val savePath = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.source}/${request.scenarioId}.json"
//        val serializedObject = jacksonObjectMapper().writeValueAsString(request.removeScenarioId())
//
//        try {
//            BufferedWriter(FileWriter(savePath)).use {
//                it.write(serializedObject)
//            }
//        } catch (e: Exception) {
//            logger.error("Failed to save revised JSON file: ${e.message.toString()}, ScenarioUUID: ${request.scenarioId}")
//        }
//    }
//
//    fun copyResourceFile(simulationId: String){
//        val fileTypes = listOf("h_logo_purple.svg", "h_logo_white.svg")
//
//        for (fileType in fileTypes) {
//            val sourceDirectory = "${loadroverConfig.gatling.imagePath}/${fileType}"
//            val targetDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.result}/${simulationId}/style/${fileType}"
//
//            val sourcePath: Path = Path.of(sourceDirectory)
//            val targetPath: Path = Path.of(targetDirectory)
//
//            try {
//                Files.copy(
//                    sourcePath,
//                    targetPath,
//                    StandardCopyOption.REPLACE_EXISTING
//                )
//            } catch (e: Exception) {
//                logger.error("Failed to save revised JSON file: ${e.message.toString()}, ScenarioUUID: $simulationId")
//            }
//        }
//    }

}