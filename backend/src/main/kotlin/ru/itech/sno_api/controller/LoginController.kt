package ru.itech.sno_api.controller


import org.mindrot.jbcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import ru.itech.sno_api.database.Users
import ru.itech.sno_api.util.Constants.EXPIRATION_TIME
import ru.itech.sno_api.plugins.features.login.LoginReciveRemoute
import ru.itech.sno_api.plugins.features.registration.RegisterResponseRemoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin() {
        val loginReciveRemoute = call.receive<LoginReciveRemoute>()
        val userDTO = Users.fetchUser(loginReciveRemoute.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.Conflict, "User not found(")
        } else {
            if (BCrypt.checkpw(loginReciveRemoute.password, userDTO.password)) {
                val token = generateToken(userDTO.login)
                call.respond(RegisterResponseRemoute(token = token))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not logged in")
            }
        }
    }

    private fun generateToken(login: String): String {
        val algorithm = Algorithm.HMAC256("your_secret")
        return JWT.create()
            .withSubject(login)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(algorithm)
    }
}
