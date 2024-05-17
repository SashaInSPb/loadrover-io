package loadrover.api.io.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    fun stringToLocalDate(date: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        return LocalDate.parse(date, formatter)
    }
}