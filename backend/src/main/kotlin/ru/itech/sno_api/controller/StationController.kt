package ru.itech.sno_api.controller

import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.itech.sno_api.plugins.features.stations.StationRequestByDate
import ru.itech.sno_api.plugins.features.stations.StationRequestDetailed
import ru.itech.sno_api.plugins.features.stations.StationResponse


class StationController(private val call: ApplicationCall) {

    suspend fun handleStationRequestByDate(request: StationRequestByDate) {
        try {
            println(request)
            val stations = Stations.fetchStationsByDate(request.data)

            call.respond(StationResponse("Success", stations = stations))
        } catch (e: Exception) {
            call.respond(StationResponse("An error occurred: ${e.localizedMessage}"))
        }
    }

    suspend fun handleStationRequestDetailed(request: StationRequestDetailed) {
        try {
            val station = Stations.fetchStationDetailed(request.timeStart, request.timeStop?: "" , request.stationName, request.branchName)
            call.respond(StationResponse("Success", station = station))
        } catch (e: Exception) {
            call.respond(StationResponse("An error occurred: ${e.localizedMessage}"))
        }
    }
}
