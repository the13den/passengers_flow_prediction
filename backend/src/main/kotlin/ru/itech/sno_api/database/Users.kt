package ru.itech.sno_api.database

import ru.itech.sno_api.dto.UserDto
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("user_table") {
    private val userId = long("user_id").autoIncrement()
    private val login = varchar("login", 255)
    private val password = varchar("password", 255)
    private val email = varchar("email", 255)

    fun insert(loginDTO: UserDto){
        transaction {
            Users.insert{
                it[userId] = loginDTO.userId
                it[password] = loginDTO.password
                it[login] = loginDTO.login
                it[email] = loginDTO.email
            }

            }
        }

    fun fetchUser(login: String): UserDto?{
        return try {
            transaction {
                val userModel = Users.select{ Users.login eq login }.single()
                UserDto(
                    login = userModel[Users.login],
                    userId = userModel[userId],
                    password = userModel[password],
                    email = userModel[email]
                )
            }
        }catch (e: Exception){

            e.printStackTrace()
            null
        }
    }

    }

