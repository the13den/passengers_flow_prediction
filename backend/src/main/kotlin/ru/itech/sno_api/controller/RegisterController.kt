package ru.itech.sno_api.controller

import org.mindrot.jbcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import ru.itech.sno_api.database.Users
import ru.itech.sno_api.dto.TokenDTO
import ru.itech.sno_api.dto.UserDto
import ru.itech.sno_api.util.Constants.EXPIRATION_TIME
import ru.itech.sno_api.plugins.features.registration.RegisterResponseRemoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*
import ru.itech.sno_api.database.Tokens
import ru.itech.sno_api.plugins.features.registration.RegisterReciveRemoute
import ru.itech.sno_api.utils.isValidEmail

class RegisterController(private val call: ApplicationCall) {
    suspend fun registerNewUser() {
        val registerReciveRemoute = call.receive<RegisterReciveRemoute>()
        if (!registerReciveRemoute.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Invalid Email")
        }
        val userDTO = Users.fetchUser(registerReciveRemoute.login)
        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "Login is already registered")
        } else {
            val hashedPassword = BCrypt.hashpw(registerReciveRemoute.password, BCrypt.gensalt())
            val token = generateToken(registerReciveRemoute.login)
            try {
                Users.insert(
                    UserDto(
                        login = registerReciveRemoute.login,
                        password = hashedPassword,
                        email = registerReciveRemoute.email,
                        userId = 0
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.BadRequest, "User is already registered")
            }
            Tokens.insert(
                TokenDTO(
                    token = token,
                    rowId = 0,
                    login = registerReciveRemoute.login,
                )
            )
            call.respond(RegisterResponseRemoute(token = token))
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
