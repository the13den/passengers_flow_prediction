package ru.itech.sno_api.plugins.features.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginReciveRemoute(
    val login: String,
    val password: String,
)

@Serializable
data class LoginResponseRemoute(
    val token: String
)
