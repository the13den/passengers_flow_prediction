package ru.itech.sno_api.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import ru.itech.sno_api.util.Constants.EXPIRATION_TIME
import java.util.*
import ru.itech.sno_api.database.Tokens

object JWTUtil {
    private const val SECRET = "your_secret"
    private val algorithm = Algorithm.HMAC256(SECRET)
    private val verifier: JWTVerifier = JWT.require(algorithm).build()

    fun generateToken(login: String): String {
        return JWT.create()
            .withSubject(login)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(algorithm)
    }

    fun validateToken(token: String): Boolean {
        return try {
            verifier.verify(token)
            true
        } catch (e: JWTVerificationException) {
            false
        }
    }

    // Функция для извлечения логина из токена
    fun getLoginFromToken(token: String): String? {
        return try {
            val jwt = verifier.verify(token)
            jwt.subject
        } catch (e: JWTVerificationException) {
            println("JWT verification failed: ${e.message}")
            null
        }
    }

    // Функция для обновления токена
    fun refreshToken(login: String, refreshToken: String): String? {
        if (!validateToken(refreshToken)) {
            return null
        }
        val oldToken = Tokens.fetchToken(login)
        println(oldToken)

        if (oldToken == null) {
            return null
        }
        val newToken = generateToken(login)
        Tokens.updateToken(login, newToken)

        return newToken
    }



}



