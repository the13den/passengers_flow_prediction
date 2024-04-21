package ru.itech.sno_api.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Test(
    val name: String
)

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("OK")
        }
    }
}
