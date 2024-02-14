package loadrover.api.io.domain.simulation

import loadrover.api.io.utils.FileUtils
import org.springframework.stereotype.Service
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class SimulationService(
    private val fileUtils: FileUtils
) {

    fun getSimulationResult(scenarioId: String): SimulationDto.ResultResponse {
        val resultList = fileUtils.searchResultFolders()
        val filteredList = resultList
            .filter { it.scenarioId.contains(scenarioId) }
            .map { it.scenarioId }

        val latestResult = filteredList.maxByOrNull { extractNumberAfterHyphen(it) }

//        // TODO: memory leak 발생
//        try {
////          zipFolder(folderPath, "$folderPath/$zipFileName")
//        } catch (e: Error) {
//            logger.error("Failed to create zip file: ${e.message}")
//        }

        return SimulationDto.ResultResponse(
            fileName = latestResult
        )
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