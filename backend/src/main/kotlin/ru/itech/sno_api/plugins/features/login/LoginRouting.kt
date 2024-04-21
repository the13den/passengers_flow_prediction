package ru.itech.sno_api.plugins.features.login

import ru.itech.sno_api.controller.LoginController
import ru.itech.sno_api.util.JWTUtil
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val loginController = LoginController(call)
            loginController.performLogin()
        }

        get("/check-token") {
            val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
            if (token != null && JWTUtil.validateToken(token)) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/refresh-token") {
            val refreshTokenRequest = call.receive<RefreshTokenRequest>()
            val refreshToken = refreshTokenRequest.refreshToken
            val login = JWTUtil.getLoginFromToken(refreshToken)
            val newToken = login?.let { JWTUtil.refreshToken(it, refreshToken) }
            if (newToken != null) {
                call.respond(mapOf("token" to newToken))
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

    }
    }
