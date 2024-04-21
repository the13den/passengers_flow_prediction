package ru.itech.sno_api.dto

class UserDto(
    val userId: Long,
    val login: String,
    val password: String,
    val email: String,
)