package ru.itech.sno_api.plugins.cashe

import ru.itech.sno_api.plugins.features.registration.RegisterReciveRemoute

data class TokenCache(
    val login: String,
    val token: String
)

object InMemoryCache {
    val userList: MutableList<RegisterReciveRemoute> = mutableListOf()
    val token: MutableList<TokenCache> = mutableListOf()
}