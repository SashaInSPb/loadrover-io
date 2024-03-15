//package loadrover.api.io.utils
//
//import loadrover.api.io.config.LoadroverConfig
//import loadrover.api.io.config.exception.ExceptionCode
//import loadrover.api.io.config.exception.NotFoundDataException
//import org.jsoup.Jsoup
//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Component
//import java.io.File
//import java.time.LocalDateTime
//
//@Component
//class HtmlUtils(
//    private val loadroverConfig: LoadroverConfig,
//    private val fileUtils: FileUtils
//) {
//    private val logger = LoggerFactory.getLogger(HtmlUtils::class.java)
//
//    fun reviseHtmlHeader(simulationId: String){
//
//        // 폴더 안에 있는 모든 html 내 헤더를 변경
//        val htmlList = fileUtils.searchHtmlFiles(simulationId).filter { it.fileName.contains(".html") }
//
//        // simulation이 비 정상적으로 실행되어 log 파일만 생성된 경우
//        if (htmlList.isEmpty()) {
//            logger.debug("Inappropriate simulation result: {}, simulationId: {}", LocalDateTime.now(), simulationId)
//        }
//
//        for (html in htmlList) {
//            val filePath = "${loadroverConfig.gatling.path}/${loadroverConfig.gatling.result}/${simulationId}/${html.fileName}"
//
//            val resultFilePath = File(filePath)
//            val resultHtml = Jsoup.parse(resultFilePath, "UTF-8")
//
//            // dom 조작
//            val gatlingLogoElements = resultHtml.select("a.gatling-logo[href='https://gatling.io'][target='blank_'][title='Gatling Home Page']")
//
//            for (element in gatlingLogoElements) {
//                element.remove()
//            }
//
//            val documentationLogo = resultHtml.select("a.gatling-documentation[href='https://gatling.io/docs/'][target='_blank']").first()
//            documentationLogo?.remove()
//
//            val enterpriseLogo = resultHtml.select("a.enterprise[href='https://gatling.io/enterprise/next-step/'][target='_blank']").first()
//            enterpriseLogo?.remove()
//
//            val wrapperClass = resultHtml.select("div.gatling-open-source")
//            wrapperClass.html("<h1 class=\"gatling-logo gatling-logo-light\"><img alt=\"2bytes\" src=\"style/h_logo_purple.svg\" style=\"width: 113%; height: auto;\"/></h1>\n<h1 class=\"gatling-logo gatling-logo-dark\"><img alt=\"2bytes\" src=\"style/h_logo_white.svg\" style=\"width: 113%; height: auto;\"/></h1>")
//
//            val outputFile = File(filePath)
//
//            try {
//                outputFile.writeText(resultHtml.outerHtml(), Charsets.UTF_8)
//
//            } catch (e: Exception) {
//                logger.error("Failed to revise result html: ${e.message.toString()}, simulationId: $simulationId - ${html.fileName}")
//            }
//
//            logger.debug("Revise result html: {}", LocalDateTime.now())
//        }
//    }
//
//}