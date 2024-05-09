package loadrover.api.io.domain.run

import loadrover.api.io.config.exception.BaseException
import loadrover.api.io.config.exception.ExceptionCode
import loadrover.api.io.config.LoadroverProperties
import loadrover.api.io.domain.task.TaskRepository
import loadrover.api.io.infra.AwsS3Service
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime

@Service
class RunService(
    private val runRepository: RunRepository,
    private val taskRepository: TaskRepository,
    private val loadroverProperties: LoadroverProperties,
    private val awsS3Service: AwsS3Service
) {
    private val logger = LoggerFactory.getLogger(RunController::class.java)

    @Async
    fun runSimulation(taskId: Long) {

        val taskEntity = taskRepository.findById(taskId).orElseThrow {
            throw BaseException(ExceptionCode.NOT_FOUND_CONTENTS)
        }

        // 현재 디렉토리
//            val currentDirectory2 = System.getProperty("java.io.tmpdir")
        val currentDirectory = System.getProperty("user.dir")
        val currentDateTime = LocalDateTime.now()

        val generatorIpList = taskEntity.generatorList.joinToString(",") { it.hostAddress }

        try {
            val jmeterScript = "./jmeter.sh"
            val downLoadFile = awsS3Service.getObject(taskEntity.uploadFileName.toString())
            val options = mutableListOf(
                "-n", "-t", downLoadFile.name,
                "-l", "$currentDateTime.jtl",
                "-e", "-o", "$currentDirectory/${loadroverProperties.reportDirectory}/${downLoadFile.name}"
            )

            // Remote 분산 테스트 옵션 추가
            if (generatorIpList !== "") options.plusAssign("-R$generatorIpList")

            val processBuilder = ProcessBuilder(jmeterScript, *options.toTypedArray())

            processBuilder.directory(
                File("$currentDirectory/${loadroverProperties.workingDirectory}")
            )

            val process = processBuilder.start()
            val exitCode = process.waitFor()

            println("Simulation process exitCode: $exitCode")

        } catch (e: Exception) {
            logger.error("Failed to run simulation: ${e.message.toString()}")
        }

        val previousRunCount = taskEntity.runCount
        val newRunCount = previousRunCount?.plus(1)
        taskEntity.runCount = newRunCount

        val runEntity = RunEntity(
            runOrder = if (previousRunCount == 0) 1 else newRunCount,
            task = taskEntity,
            hostIp = generatorIpList
        )

        try {
            runRepository.save(runEntity)
            taskRepository.save(taskEntity)

        } catch (e: Exception) {
            logger.error("Failed to save entity: ${e.message.toString()}, taskId: $taskId")
        }
    }
}