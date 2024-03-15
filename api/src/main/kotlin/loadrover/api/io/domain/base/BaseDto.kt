package loadrover.api.io.domain.base

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class BaseDto {

    data class FileDto(
        val upLoadFile: MultipartFile
    ) {
        val originalFile: String = upLoadFile.originalFilename!!
        val contentType: String = upLoadFile.contentType!!
        val size: Long = upLoadFile.size
        val fileName: String = originalFile.substring(0, originalFile.indexOf("."))
        val extension: String = originalFile.substring(originalFile.lastIndexOf(".") + 1)
        val convertFileName: String = "${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"))} - ${this.fileName} - ${UUID.randomUUID()}-${System.nanoTime()}"
        val uploadFileName: String = "$convertFileName.$extension"

        fun getUploadPath(): String {
            return uploadFileName
        }

        fun getContentsFromFile(upLoadFile: MultipartFile, limit: Int): String {
            val inputStream = upLoadFile.inputStream
            val bufferedReader = inputStream.bufferedReader()
            val charArray = CharArray(limit)
            bufferedReader.read(charArray, 0 , limit)
            return String(charArray)
        }
    }

    data class UploadResultDto(
        val fullPath: String,
        val path: String
    )

}