package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.*
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
        val resultList = fileUtils.searchFolders()
        var htmlPath = ""
        var filePath = ""

        // 정리필요
        for (result in resultList) {
            if (result.scenarioId.contains(scenarioId)) {
                val resultDirectory = "${loadroverConfig.gatling.result}/${result.scenarioId}"
                val htmlDirectory = "$resultDirectory/index.html"

                val zipFileName = "${result.scenarioId.replace("-\\d+".toRegex(),"")}.zip"
                val zipFileDirectory = "$resultDirectory/$zipFileName"
                val folderPath = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.result}/${result.scenarioId}"

                // TODO: memory leak 발생
                try {
                    zipFolder(folderPath, "$folderPath/$zipFileName")
                } catch (e: Error) {
                    log.error("Failed to create zip file: ${e.message}, scenarioId: $folderPath/$zipFileName")
                }

                htmlPath = ServletUriComponentsBuilder.fromCurrentContextPath().path(htmlDirectory).toUriString()
                filePath = ServletUriComponentsBuilder.fromCurrentContextPath().path(zipFileDirectory).toUriString()
            }
        }

        return SimulationDto.ResultResponse(
            filePath = filePath,
            htmlPath = htmlPath
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
    fun zipFolder(folderPath: String, zipFilePath: String) {
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

}