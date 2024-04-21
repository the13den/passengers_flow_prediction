package ru.itech.sno_api.dto

import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlinx.serialization.Contextual

@Serializable
data class StationDTO (
    val id: Long,
    val stationName: String,
    val branchName: String,
    val timeStart: String,
    val timeStop: String,
    val workload: Long
)
