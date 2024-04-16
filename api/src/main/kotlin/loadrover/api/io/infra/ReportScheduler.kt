package loadrover.api.io.infra

import loadrover.api.io.config.LoadroverProperties
import loadrover.api.io.config.exception.BaseException
import loadrover.api.io.config.exception.ExceptionCode
import loadrover.api.io.domain.task.TaskRepository
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.io.File
import java.time.LocalDateTime

@EnableScheduling
@SpringBootApplication
class ReportScheduler(
    private val fileUtils: FileUtils,
    private val loadroverProperties: LoadroverProperties,
    private val awsS3Service: AwsS3Service,
    private val taskRepository: TaskRepository
) {
    private val logger = LoggerFactory.getLogger(ReportScheduler::class.java)
    @Scheduled(cron = "0 */1 * * * *") // 매 1분으로 설정
    fun checkReport() {

        val reportDirList = fileUtils.searchDirectories(loadroverProperties.reportDirectory)
        val reportDirPath = loadroverProperties.reportDirectory

        for (report in reportDirList) {
            val fileName = report.resultFolderName
            val fileNameNoExtension = fileName.substring(0, fileName.indexOf("."))

            fileUtils.zipAll(
                "$reportDirPath/${report.resultFolderName}",
                "$reportDirPath/${fileNameNoExtension}.zip"
            )

            val file = File("$reportDirPath/${fileNameNoExtension}.zip")
            val s3Url = awsS3Service.upload(file.name, file.inputStream())

            val taskEntity = taskRepository.findByUploadFileName(fileName).orElseThrow {
                throw BaseException(ExceptionCode.NOT_FOUND_CONTENTS)
            }

            taskEntity.reportPath = s3Url.fullPath

            try {
                taskRepository.save(taskEntity)

            } catch (e: Exception) {
                logger.error("Failed to update task: ${e.message.toString()}, taskId: ${taskEntity.id}")
                throw BaseException(ExceptionCode.UPDATE_FAIL)
            }
            fileUtils.deleteDirectory("$reportDirPath/${report.resultFolderName}")
        }

        logger.debug("Check report: {}", LocalDateTime.now())

    }
}