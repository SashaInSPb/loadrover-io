package loadrover.api.io.page

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @GetMapping("/")
    fun defaultPage(): String {
        return "views/home"
    }

}