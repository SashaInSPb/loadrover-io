//package loadrover.api.io.domain.simulation
//
//import loadrover.api.io.utils.FileUtils
//import org.slf4j.LoggerFactory
//import org.springframework.core.io.DefaultResourceLoader
//import org.springframework.core.io.support.ResourcePatternUtils
//import org.springframework.scheduling.annotation.Async
//import org.springframework.stereotype.Service
//import java.io.*
//import java.util.zip.ZipEntry
//import java.util.zip.ZipOutputStream
//
//
//@Service
//class SimulationService(
//    private val fileUtils: FileUtils
//) {
//    private val logger = LoggerFactory.getLogger(SimulationService::class.java)
//
//    fun getSimulationResult(scenarioId: String): SimulationDto.ResultResponse {
//        val resultList = fileUtils.searchResultDirectories()
//        val filteredList = resultList
//            .filter { it.scenarioId.contains(scenarioId) }
//            .map { it.scenarioId }
//
//        val latestResult = filteredList.maxByOrNull { extractNumberAfterHyphen(it) }
//
////        try {
//////          zipAll(folderPath, "$folderPath/$zipFileName")
////
////        } catch (e: Error) {
////            logger.error("Failed to create zip file: ${e.message}")
////        }
//
//        return SimulationDto.ResultResponse(
//            fileName = latestResult
//        )
//    }
//
//    fun zipAll(directory: String, zipFile: String) {
//        val sourceFile = File(directory)
//
//        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use {
//            zipFiles(it, sourceFile, "")
//        }
//    }
//
//    fun zipFiles(zipOut: ZipOutputStream, sourceFile: File, parentDirPath: String) {
//        val data = ByteArray(2048)
//
//        sourceFile.listFiles()?.forEach { f ->
//            if (f.isDirectory) {
//                val path = if (parentDirPath == "") f.name else parentDirPath + File.separator + f.name
//                val entry = ZipEntry(path + File.separator)
//                entry.time = f.lastModified()
//                entry.isDirectory
//                entry.size = f.length()
//                zipFiles(zipOut, f, path)
//
//            } else {
//                FileInputStream(f).use { fi ->
//                    BufferedInputStream(fi).use { origin ->
//                        val path = parentDirPath + File.separator + f.name
//                        val entry = ZipEntry(path)
//                        entry.time = f.lastModified()
//                        entry.isDirectory
//                        entry.size = f.length()
//                        zipOut.putNextEntry(entry)
//                        while (true) {
//                            val readBytes = origin.read(data)
//                            if (readBytes == -1) break
//                            zipOut.write(data, 0, readBytes)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Async
//    // gatling shell script 실행 함수
//    fun executeGatlingScript(scenarioId: String) {
//        try {
//            val processBuilder = ProcessBuilder(
//                "./gradlew",
//                ":gatling:gatlingRun-work.$scenarioId",
//                "-stacktrace"
//            )
//
//            // 프로젝트 root dir로 process 실행 설정
//            val resources = ResourcePatternUtils.getResourcePatternResolver(DefaultResourceLoader())
//                .getResources("classpath*:gatling/**")
//
//            val resourceFile = File(resources.toString())
//
//
//            processBuilder.directory(
//                resourceFile.parentFile
//            )
//
//            // log 설정
//            val logFile = File("logs/simulation/$scenarioId.log")
//            processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile))
//            processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(logFile))
//
//            val process = processBuilder.start()
//            val exitCode = process.waitFor()
//
//            println("Process exitCode: $exitCode")
//
//        } catch (e: Exception) {
//            logger.error("Failed to run: ${e.message.toString()}, scenarioUUID: $scenarioId")
//        }
//    }
//
//    private fun extractNumberAfterHyphen(folderName: String): Long {
//        val hyphenIndex = folderName.indexOf('-')
//        if (hyphenIndex != -1 && hyphenIndex < folderName.length -1) {
//            val numberString = folderName.substring(hyphenIndex +1 )
//            return numberString.toLongOrNull() ?: 0
//        }
//        return 0
//    }
//
//}