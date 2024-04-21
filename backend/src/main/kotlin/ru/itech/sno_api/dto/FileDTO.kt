package ru.itech.sno_api.dto

import io.swagger.v3.oas.annotations.media.Schema

data class FilesDTO(
    @Schema(description = "Идентификатор файла")
    val fileId: Long,
    @Schema(description = "Путь к файлу")
    val filePath: String,
    @Schema(description = "Лекция")
    val userId: Long
)
