package loadrover.api.io.domain.simulation

import io.swagger.v3.oas.annotations.media.Schema

class SimulationDto {

    @Schema(description = "부하테스트 실행 request")
    data class RunSimulationRequest(
        val scenarioId: String
    )

    @Schema(description = "부하테스트 결과 response")
    data class ResultResponse(
        @Schema(description = "시뮬레이션 결과 파일 UUID", example = "test3820240129150504424-2024013289301893021")
        val fileName: String? = "",
    )
}