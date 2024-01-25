package loadrover.api.io.domain.simulation

import io.swagger.v3.oas.annotations.media.Schema

class SimulationDto {

    data class RunSimulationRequest(
        val scenarioId: String
    )

    @Schema(description = "시뮬레이션 결과 html url")
    data class ResultResponse(
        @Schema(description = "시뮬레이션 결과 html url", example = "Test420240124205133197")
        val htmlPath: String
    )
}