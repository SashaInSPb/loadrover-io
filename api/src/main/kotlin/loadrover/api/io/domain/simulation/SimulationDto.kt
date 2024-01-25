package loadrover.api.io.domain.simulation

import io.swagger.v3.oas.annotations.media.Schema

class SimulationDto {

    data class RunSimulationRequest(
        val scenarioId: String
    )

    @Schema(description = "시뮬레이션 결과 html url")
    data class ResultResponse(
        @Schema(description = "시뮬레이션 결과 html url", example = "http://10.88.12.188:3020/result/Test420240124205133197-20240124115720736/index.html")
        val htmlPath: String
    )
}