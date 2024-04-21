package ru.itech.sno_api.plugins.features.registration

import ru.itech.sno_api.controller.RegisterController
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()
        }

    }

}