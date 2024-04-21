package ru.itech.sno_api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*

import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import ru.itech.sno_api.plugins.*
import ru.itech.sno_api.plugins.features.documentation.configureDocumentationRouting
import ru.itech.sno_api.plugins.features.file.FileRemote
import ru.itech.sno_api.plugins.features.file.configureFileRouting
import ru.itech.sno_api.plugins.features.login.configureLoginRouting
import ru.itech.sno_api.plugins.features.registration.configureRegisterRouting
import ru.itech.sno_api.plugins.features.stations.configureStationRouting
import java.io.File

fun main() {
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/mos_trans_project_db",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "admin",
        password = "1357"
    )

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/mos_trans_project_db",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "admin",
        password = "1357"
    )
    val fileRemote = FileRemote(database)

    configureFileRouting(fileRemote)
    configureStationRouting()
    configureDocumentationRouting()
    configureRouting()
    configureLoginRouting()
    configureRegisterRouting()
    configureSerialization()

    val client = HttpClient(CIO) {
        install(HttpRedirect)
    }

    routing {
        // Маршрут для перенаправления
        post("/stations/by-date") {
            val id = call.parameters["id"]
            // Перенаправление на новый URL
            call.respondRedirect("http://127.0.0.1:5000/station/bydate")
        }

        // Обработка запросов к /files/
        get("/files/{id}") {
            val id = call.parameters["id"]
            // Отправляем ответ обратно клиенту
            call.respondText("Hello from /files/$id", ContentType.Text.Plain)
        }
    }
}
