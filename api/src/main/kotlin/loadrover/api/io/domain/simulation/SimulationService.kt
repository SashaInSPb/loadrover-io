package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import loadrover.api.io.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SimulationService(
    private val fileUtils: FileUtils,
    private val loadroverConfig: LoadroverConfig
) {
    private val log = LoggerFactory.getLogger(SimulationService::class.java)

    fun getSimulationResult(scenarioId: String): SimulationDto.ResultResponse {
        // 같은 scenarioID가 있는 경우?
        //

        return SimulationDto.ResultResponse(
            htmlPath = "",
            filePath = ""
        )
    }


}