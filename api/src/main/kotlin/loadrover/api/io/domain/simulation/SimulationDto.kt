package loadrover.api.io.domain.simulation

class SimulationDto {

    data class RunSimulationRequest(
        val scenarioId: String
    )

    data class ResultResponse(
        val htmlPath: String
    )
}