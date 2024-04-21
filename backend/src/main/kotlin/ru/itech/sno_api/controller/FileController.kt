package ru.itech.sno_api.controller


import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class FileController(private val call: ApplicationCall) {
    fun getFileId(): Long? {
        return call.parameters["fileId"]?.toLongOrNull()
    }
}