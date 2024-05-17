package loadrover.api.io.utils

import com.google.gson.Gson
import org.springframework.boot.autoconfigure.gson.GsonProperties

object StringUtils {
    fun objectToJsonString(T: Any): String {
        val gson = Gson()
        return gson.toJson(T)
    }
}