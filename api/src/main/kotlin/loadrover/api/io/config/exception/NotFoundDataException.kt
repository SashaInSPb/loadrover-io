package loadrover.api.io.config.exception

class NotFoundDataException(exceptionCode: ExceptionCode): RuntimeException() {
    val baseResponseCode: ExceptionCode = exceptionCode
    override val message: String = this.baseResponseCode.message
}