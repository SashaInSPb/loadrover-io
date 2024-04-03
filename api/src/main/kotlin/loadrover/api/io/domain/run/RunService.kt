package loadrover.api.io.domain.run

import loadrover.api.io.config.exception.BaseException
import loadrover.api.io.config.exception.ExceptionCode
import loadrover.api.io.domain.task.TaskRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime

@Service
class RunService(
    private val runRepository: RunRepository,
    private val taskRepository: TaskRepository
) {
    private val logger = LoggerFactory.getLogger(RunController::class.java)

    @Async
    fun runSimulation(taskId: Long) {

        val currentDateTime = LocalDateTime.now()

        try {
            val currentDirectory = System.getProperty("user.dir")

            val jmeterScript = "./jmeter.sh"
            val options = listOf("-n", "-t", "test.jmx", "-l", "$currentDateTime.jtl")
            val processBuilder = ProcessBuilder(jmeterScript, *options.toTypedArray())

            // bin directory로 변경
            processBuilder.directory(File("$currentDirectory/api/build/resources/main/static/apache-jmeter-5.6.3/bin"))

            val process = processBuilder.start()
            val exitCode = process.waitFor()

            println("Simulation process exitCode: $exitCode")

        } catch (e: Exception) {
            logger.error("Failed to run simulation: ${e.message.toString()}")
        }

        // RunEntity 저장
        val taskEntity = taskRepository.findById(taskId).orElseThrow {
            throw BaseException(ExceptionCode.NOT_FOUND_CONTENTS)
        }

        val previousRunCount = taskEntity.runCount
        val newRunCount = previousRunCount?.plus(1)
        val hostIpList = ""

        for (generator in taskEntity.generatorList) {
            hostIpList.plus(generator.hostAddress)
        }

        val runEntity = RunEntity(
            runOrder = if (previousRunCount == 0) 1 else newRunCount,
            task = taskEntity,
            hostIp = hostIpList
        )

        try {
            runRepository.save(runEntity)

        } catch (e: Exception) {
            logger.error("Failed to save runEntity: ${e.message.toString()}, taskId: $taskId")
        }
    }
}