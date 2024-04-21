package ru.itech.sno_api.plugins.features.stations




import kotlinx.serialization.Serializable
import ru.itech.sno_api.dto.StationDTO


@Serializable
data class StationResponse(
    val message: String,
    val station: StationDTO? = null,
    val stations: List<StationDTO>? = null
)
