package ru.itech.sno_api.plugins.features.stations


import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.itech.sno_api.controller.StationController

fun Application.configureStationRouting() {
    routing {
        post("/stations/by-date") {
            val request = call.receive<StationRequestByDate>()
            val controller = StationController(call)
            controller.handleStationRequestByDate(request)
        }

        post("/stations/detailed") {
            val request = call.receive<StationRequestDetailed>()
            val controller = StationController(call)
            controller.handleStationRequestDetailed(request)
        }
    }
}
