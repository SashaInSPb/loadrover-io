package loadrover.api.io.config

import loadrover.api.io.config.exception.BaseException
import loadrover.api.io.config.exception.NotFoundDataException
import org.apache.logging.log4j.util.StringMap
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class WebApiExceptionHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    fun handleAll(exception: Exception, request: WebRequest): ResponseEntity<Any> {
        val apiError = ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            exception.localizedMessage,
            "Error occurred"
        )

        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<Any> {
        val apiError = ApiError(e.baseResponseCode.httpStatus, e.baseResponseCode.message, "errorCode: ${e.baseResponseCode.httpStatus.value()}", e.baseResponseCode.httpStatus.value().toString())
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }

    @ExceptionHandler(NotFoundDataException::class)
    fun handleNotFoundDataException(e: NotFoundDataException): ResponseEntity<Any> {
        val apiError = ApiError(e.baseResponseCode.httpStatus, e.baseResponseCode.message, "204 error")
        return ResponseEntity(apiError, HttpHeaders(), apiError.status)
    }
}

data class ApiError(
    var status: HttpStatus,
    var clientMessage: String,
    var errors: List<String>,
    var developerMessage: String? = null
) {
    constructor(status: HttpStatus,clientMessage: String, error: String, developerMessage: String? = null):
            this(status, clientMessage, arrayListOf<String>(error), developerMessage)

}
