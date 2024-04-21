package ru.itech.sno_api.plugins.features.file

import ru.itech.sno_api.database.Files
import ru.itech.sno_api.dto.FilesDTO
import org.jetbrains.exposed.sql.Database


class FileRemote(private val database: Database) {

    fun retrieveFile(fileId: Long): FilesDTO? {
        return Files.fetchFilesForLecture(fileId).firstOrNull()
    }

    fun update(fileId: Long, fileDTO: FilesDTO) {
        return Files.update(fileId, fileDTO)
    }

    fun delete(fileId: Long) {
        return Files.delete(fileId)
    }
    fun insert(fileDTO: FilesDTO) {
        return Files.insert(fileDTO)
    }
}
