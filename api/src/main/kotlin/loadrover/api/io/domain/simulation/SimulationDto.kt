package loadrover.api.io.domain.simulation

import io.swagger.v3.oas.annotations.media.Schema

class SimulationDto {

    data class RunSimulationRequest(
        val scenarioId: String
    )

    @Schema(description = "시뮬레이션 결과 html url")
    data class ResultResponse(
        @Schema(description = "시뮬레이션 결과 파일 UUID", example = "test3820240129150504424-2024013289301893021")
        val fileName: String? = "",
    )
}