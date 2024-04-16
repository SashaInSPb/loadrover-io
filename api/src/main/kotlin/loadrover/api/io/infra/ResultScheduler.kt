package loadrover.api.io.infra

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@EnableScheduling
@SpringBootApplication
class ResultScheduler(
) {
    private val logger = LoggerFactory.getLogger(ResultScheduler::class.java)

    // result 디렉토리를 뒤진다.
    // result 파일명으로

    @Scheduled(cron = "0 */1 * * * *") // 매 1분으로 설정
    fun uploadReport() {
//
//        val resultFolderList = fileUtils.searchResultDirectories()


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