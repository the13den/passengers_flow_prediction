package ru.itech.sno_api.plugins.features.registration

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReciveRemoute(
    val login: String,
    val email: String,
    val password: String

)

@Serializable
data class RegisterResponseRemoute(
    val token: String,
)