package loadrover.api.io.page

import jakarta.servlet.http.HttpServletResponse
import org.apache.http.HttpResponse
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ExceptionController: ErrorController {

    @GetMapping("/error")
    fun error(httpResponse: HttpServletResponse): String {
        return when (httpResponse.status) {
            400 -> "views/error/error-401"
            401 -> "views/error/error-401"
            404 -> "views/error/error-404"
            500 -> "views/error/error-500"
            else -> "views/error/error-500"
        }
    }

    @GetMapping("/error/404")
    fun error404(): String {
        return "views/error/error-404"
    }

}