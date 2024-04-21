package ru.itech.sno_api.plugins.features.file

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.itech.sno_api.dto.FilesDTO
import java.io.File
import java.nio.file.Files




fun Application.configureFileRouting(fileRemote: FileRemote) {
    routing {
        get("/file/{fileId}") {
            val fileId = call.parameters["fileId"]?.toLongOrNull()
            if (fileId != null) {
                val fileDTO = fileRemote.retrieveFile(fileId)
                if (fileDTO != null) {
                    val file = File(fileDTO.filePath)
                    if (file.exists()) {
                        val contentType = ContentType.parse(Files.probeContentType(file.toPath()) ?: "application/octet-stream")
                        call.response.header(HttpHeaders.ContentDisposition, "attachment; filename=\"${file.name}\"")
                        call.respondFile(file)
                    } else {
                        call.respondText("File not found", status = HttpStatusCode.NotFound)
                    }
                } else {
                    call.respondText("File not found", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Invalid fileId", status = HttpStatusCode.BadRequest)
            }
        }

        get("/stream/{fileId}") {
            val fileId = call.parameters["fileId"]?.toLongOrNull()
            if (fileId != null) {
                val fileDTO = fileRemote.retrieveFile(fileId)
                if (fileDTO != null) {
                    val file = File(fileDTO.filePath)
                    if (file.exists()) {
                        val contentType = ContentType.parse(Files.probeContentType(file.toPath()) ?: "application/octet-stream")
                        call.response.header(HttpHeaders.ContentType, contentType.toString())
                        call.respondFile(file)
                    } else {
                        call.respondText("File not found", status = HttpStatusCode.NotFound)
                    }
                } else {
                    call.respondText("File not found", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Invalid fileId", status = HttpStatusCode.BadRequest)

            }

        }
        put("/file/{fileId}") {
            val fileId = call.parameters["fileId"]?.toLongOrNull()
            val fileDTO = call.receive<FilesDTO>()
            if (fileId != null) {
                fileRemote.update(fileId, fileDTO)
                call.respondText("File updated successfully")
            } else {
                call.respondText("Invalid fileId", status = HttpStatusCode.BadRequest)
            }
        }

        delete("/file/{fileId}") {
            val fileId = call.parameters["fileId"]?.toLongOrNull()
            if (fileId != null) {
                fileRemote.delete(fileId)
                call.respondText("File deleted successfully")
            } else {
                call.respondText("Invalid fileId", status = HttpStatusCode.BadRequest)
            }
        }


        post("/upload") {
            val multipart = call.receiveMultipart()
            var fileId: Long? = null
            var filePath: String? = null
            var lectureId: Long? = null

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "fileId" -> fileId = part.value.toLongOrNull()
                            "lectureId" -> lectureId = part.value.toLongOrNull()
                        }
                    }
                    is PartData.FileItem -> {
                        val originalFileName = part.originalFileName ?: "file"
                        val contentType = part.contentType ?: ContentType.Application.OctetStream
                        val fileBytes = part.streamProvider().readBytes()

                        // Сохраняем файл на сервере
                        val directory = File("uploads")
                        if (!directory.exists()) {
                            directory.mkdir()
                        }
                        val file = File(directory, originalFileName)
                        file.writeBytes(fileBytes)

                        filePath = file.absolutePath
                    }

                    is PartData.BinaryChannelItem -> TODO()
                    is PartData.BinaryItem -> TODO()
                }
                part.dispose()
            }

            if (fileId != null && lectureId != null && filePath != null) {
                val fileDTO = FilesDTO(fileId!!, filePath!!, lectureId!!)
                fileRemote.insert(fileDTO)
                call.respondText("File uploaded successfully")
            } else {
                call.respondText("Invalid parameters for file upload", status = HttpStatusCode.BadRequest)
            }
        }
    }


}



