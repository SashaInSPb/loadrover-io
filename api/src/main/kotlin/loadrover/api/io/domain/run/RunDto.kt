package loadrover.api.io.domain.run

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

class RunDto {

    @Schema(description = "시뮬레이션 작동 response")
    data class RunSimulationResponse(
        val runId: Long
    )

}
