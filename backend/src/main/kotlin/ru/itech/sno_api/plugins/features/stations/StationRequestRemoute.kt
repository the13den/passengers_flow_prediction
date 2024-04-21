package ru.itech.sno_api.plugins.features.stations


import kotlinx.serialization.Serializable
import ru.itech.sno_api.dto.StationDTO
import java.time.LocalDate

@Serializable
data class StationRequestByDate(
    val data: String
)

@Serializable
data class StationRequestDetailed(
    val timeStart: String,
    val timeStop:String?,
    val stationName: String,
    val branchName: String
)


