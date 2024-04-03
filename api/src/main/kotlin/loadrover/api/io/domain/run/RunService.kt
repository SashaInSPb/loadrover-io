package loadrover.api.io.domain.run

import loadrover.api.io.config.exception.BaseException
import loadrover.api.io.config.exception.ExceptionCode
import loadrover.api.io.domain.task.TaskRepository
import org.slf4j.LoggerFactory
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.jvm.Throws

@Service
class RunService(
    private val runRepository: RunRepository,
    private val taskRepository: TaskRepository
) {
    private val logger = LoggerFactory.getLogger(RunController::class.java)

    @Async
    fun runSimulation(taskId: Long) {

        val log = LocalDateTime.now()
        val host = ""

        try {
            val currentDirectory = System.getProperty("user.dir")
            println("Current directory: $currentDirectory")

            val jmeterScript = "./jmeter.sh"
            val options = listOf("-n", "-t", "test.jmx", "-l", "2024-04-03T15:58:56.697422.jtl")
            val processBuilder = ProcessBuilder(jmeterScript, *options.toTypedArray())
            processBuilder.directory(File("$currentDirectory/api/build/resources/main/static/apache-jmeter-5.6.3/bin"))

//             bin root로 설정
//            val resources = ResourcePatternUtils.getResourcePatternResolver(DefaultResourceLoader())
//                .getResources("classpath*:**/user.properties")

//            val resourceFile = File(resources.toString())

//            // log 설정
//            processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile))
//            processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(logFile))

//            println("resourceFile: ${resourceFile.path}")

            // jar 파일 경로
//            processBuilder.directory(
//                resourceFile
//            )

            val process = processBuilder.start()
            val exitCode = process.waitFor()

            println("Test process exitCode: $exitCode")

        } catch (e: Exception) {
            logger.error("Failed to run: ${e.message.toString()}")
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
            logger.error("Failed to run: ${e.message.toString()}, taskId: $taskId")
        }
    }
}