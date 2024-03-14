package loadrover.api.io.config.exception

import org.springframework.http.HttpStatus

enum class ExceptionCode(httpStatusCode: HttpStatus, message: String) {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생하였습니다."), // 500
    NOT_FOUND_CONTENTS(HttpStatus.NOT_FOUND, "데이터가 존재하지 않습니다."), // 404
    CREATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "등록에 실패하였습니다."), // 500
    UPDATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "수정에 실패하였습니다."), // 500
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 값 요청입니다."), // 400
    UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드에 실패하였습니다"); // 요거 확인좀

    val httpStatus: HttpStatus = httpStatusCode
    val message: String = message
}