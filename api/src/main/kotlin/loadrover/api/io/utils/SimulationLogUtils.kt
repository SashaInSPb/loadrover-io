package loadrover.api.io.utils

import org.springframework.stereotype.Component
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Component
class SimulationLogUtils {

    fun createLogFile(scenarioId: String, text: String) {
        val logFile = File("logs/simulation/${scenarioId}.txt")

        if(!logFile.exists()) {
            logFile.createNewFile()
        }

        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
        val formattedTime = currentTime.format(formatter)

        val log = "$formattedTime  - - $text"

        try {
            val fileWriter = FileWriter(logFile, true)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(log)
            bufferedWriter.newLine()
            bufferedWriter.close()

        } catch (e: Exception) {
            println("e: ${e.message.toString()}")
        }
    }

}