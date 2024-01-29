package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class SimulationService(
    private val fileUtils: FileUtils,
    private val loadroverConfig: LoadroverConfig
) {
    private val log = LoggerFactory.getLogger(SimulationService::class.java)

    fun getSimulationResult(scenarioId: String): SimulationDto.ResultResponse {
        val resultList = fileUtils.searchResultFolders()

        // 정리필요
        // 숫자비교해서 큰 값으로 보내줄 것
        val filterList = resultList
            .filter { it.scenarioId.contains(scenarioId) }
            .map { it.scenarioId }

        val latestResult = filterList.maxByOrNull { extractNumberAfterHyphen(it) }

//        // TODO: memory leak 발생
//        try {
////                    zipFolder(folderPath, "$folderPath/$zipFileName")
//        } catch (e: Error) {
//            log.error("Failed to create zip file: ${e.message}")
//        }


        return SimulationDto.ResultResponse(
            fileName = latestResult,
        )
    }

    fun moveReadyToProgress(scenarioId: String) {
        val sourceDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.source}/${scenarioId}.json"
        val progressDirectory = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.progress}/${scenarioId}.json"

        val sourcePath: Path = Path.of(sourceDirectory)
        val progressPath: Path = Path.of(progressDirectory)

        try {
            Files.move(
                sourcePath,
                progressPath,
                StandardCopyOption.REPLACE_EXISTING
            )
        } catch (e: Exception) {
            log.error("Failed to move file: ${e.message}")
        }
    }

    // TODO: 프로세서가 멈추지 않는 문제 발생
    private fun zipFolder(folderPath: String, zipFilePath: String) {
        FileOutputStream(zipFilePath).use { fos ->
            ZipOutputStream(fos).use { zos ->
                val sourceFile = File(folderPath)
                zipFile(sourceFile, "", zos)
            }
        }
    }

    private fun zipFile(fileToZip: File, parentPath: String, zipOut: ZipOutputStream) {
        val filePath = if (parentPath.isNotEmpty()) "$parentPath/${fileToZip.name}" else fileToZip.name

        if (fileToZip.isDirectory) {
            val entries = fileToZip.listFiles() ?: return
            for (childFile in entries) {
                zipFile(childFile, filePath, zipOut)
            }
        } else {
            FileInputStream(fileToZip).use { fis ->
                BufferedInputStream(fis).use { bis ->
                    val zipEntry = ZipEntry(filePath)
                    zipOut.putNextEntry(zipEntry)
                    bis.copyTo(zipOut)
                    zipOut.closeEntry()
                }
            }
        }
    }

    private fun extractNumberAfterHyphen(folderName: String): Long {
        val hyphenIndex = folderName.indexOf('-')
        if (hyphenIndex != -1 && hyphenIndex < folderName.length -1) {
            val numberString = folderName.substring(hyphenIndex +1 )
            return numberString.toLongOrNull() ?: 0
        }
        return 0
    }

}