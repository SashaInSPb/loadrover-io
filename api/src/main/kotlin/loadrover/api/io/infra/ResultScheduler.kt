package loadrover.api.io.infra

import loadrover.api.io.config.LoadroverProperties
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.io.File
import java.time.LocalDateTime
import java.util.zip.ZipOutputStream

@EnableScheduling
@SpringBootApplication
class ResultScheduler(
    private val fileUtils: FileUtils,
    private val loadroverProperties: LoadroverProperties,
) {
    private val logger = LoggerFactory.getLogger(ResultScheduler::class.java)

    // report 디렉토리를 뒤진다.
    // report 파일명으로

    @Scheduled(cron = "0 */1 * * * *") // 매 1분으로 설정
    fun uploadReport() {
//
        val reportDirList = fileUtils.searchDirectories(loadroverProperties.reportDirectory)
        val parentDirPath = loadroverProperties.reportDirectory

//        if (reportDirectoryList.isNotEmpty())

        for (report in reportDirList) {

            fileUtils.zipFiles(ZipOutputStream, File(report.resultFolderName), parentDirPath)


        }

//
//        for (progressFile in progressFileList) {
//            val scenarioId = progressFile.scenarioId
//
//            for (resultFolder in resultFolderList) {
//                val resultFileId = resultFolder.scenarioId.replace("-\\d+".toRegex(),"")
//
//                if (resultFileId == scenarioId) {
//                    fileUtils.moveJsonFile(scenarioId, loadroverConfig.gatling.progress, loadroverConfig.gatling.complete)
//                }
//            }
//        }
        logger.debug("Check run result: {}", LocalDateTime.now())
    }

}