package ru.itech.sno_api.database


import ru.itech.sno_api.dto.TokenDTO
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


object Tokens : Table("token") {
    private val rowId = long("id").autoIncrement()
    private val login = varchar("login", 255)
    private val token = varchar("token", 255)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[this.login] = tokenDTO.login
                it[this.token] = tokenDTO.token
                it[this.rowId] = tokenDTO.rowId
            }
        }
    }

    fun fetchToken(login: String): TokenDTO? {
        return transaction {
            Tokens.select { Tokens.login eq login }.mapNotNull { row ->
                TokenDTO(
                    rowId = row[Tokens.rowId],
                    token = row[Tokens.token],
                    login = row[Tokens.login]
                )
            }.singleOrNull()
        }
    }

    fun updateToken(login: String, newToken: String) {
        transaction {
            Tokens.update({ Tokens.login eq login }) {
                it[Tokens.token] = newToken
            }
        }
    }
}
