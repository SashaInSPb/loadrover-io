package loadrover.api.io.domain.simulation

import loadrover.api.io.config.LoadroverConfig
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SimulationService(
    private val loadroverConfig: LoadroverConfig
) {
    private val log = LoggerFactory.getLogger(SimulationService::class.java)

    fun getSimulationResult() {
        // html과 폴더를 한꺼번에 내려주기
        return
    }
}