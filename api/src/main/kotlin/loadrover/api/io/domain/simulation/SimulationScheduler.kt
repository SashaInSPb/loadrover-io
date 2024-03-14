//package loadrover.api.io.domain.simulation
//
//import loadrover.api.io.config.LoadroverConfig
//import loadrover.api.io.utils.FileUtils
//import loadrover.api.io.utils.HtmlUtils
//import org.slf4j.LoggerFactory
//import org.springframework.boot.autoconfigure.SpringBootApplication
//import org.springframework.scheduling.annotation.EnableScheduling
//import org.springframework.scheduling.annotation.Scheduled
//import java.time.LocalDateTime
//
//@EnableScheduling
//@SpringBootApplication
//class SimulationScheduler(
//    private val fileUtils: FileUtils,
//    private val loadroverConfig: LoadroverConfig,
//    private val htmlUtils: HtmlUtils
//) {
//    private val logger = LoggerFactory.getLogger(SimulationScheduler::class.java)
//
//    @Scheduled(cron = "0 */1 * * * *") // 매 1분으로 설정
//    fun moveProgressToComplete() {
//        val progressFileList = fileUtils.searchFiles("progress")
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
//                    // result html 헤더 수정
//                    fileUtils.copyResourceFile(resultFolder.scenarioId)
//                    htmlUtils.reviseHtmlHeader(resultFolder.scenarioId)
//                }
//            }
//        }
//        logger.debug("Check simulation result: {}", LocalDateTime.now())
//    }
//
//}