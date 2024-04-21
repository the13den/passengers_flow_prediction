package ru.itech.sno_api.database

import org.jetbrains.exposed.sql.*
import ru.itech.sno_api.dto.FilesDTO
import org.jetbrains.exposed.sql.transactions.transaction

object Files : Table("files") {
    private val fileId = long("file_id").autoIncrement()
    private val filePath = varchar("file_path", 255)
    private val userId = long("user_id")

    fun insert(fileDTO: FilesDTO){
        transaction {
            Files.insert{
                it[this.fileId] = fileDTO.fileId
                it[this.filePath] = fileDTO.filePath
                it[this.userId] = fileDTO.userId
            }
        }
    }
    fun update(fileId: Long, fileDTO: FilesDTO){
        transaction {
            Files.update({ Files.fileId eq fileId }) {
                it[this.filePath] = fileDTO.filePath
                it[this.userId] = fileDTO.userId
            }
        }
    }

    fun insertFile(fileDTO: FilesDTO) {
        transaction {
            Files.insert {
                it[this.fileId] = fileDTO.fileId
                it[this.filePath] = fileDTO.filePath
                it[this.userId] = fileDTO.userId
            }
        }
    }
    fun delete(fileId: Long){
        transaction {
            Files.deleteWhere { Files.fileId eq fileId }
        }
    }

    fun fetchFilesForLecture(lectureId: Long): List<FilesDTO> {
        return transaction {
            Files.select { Files.userId eq lectureId }.map {
                FilesDTO(
                    fileId = it[fileId],
                    filePath = it[filePath],
                    userId = it[Files.userId]
                )
            }
        }
    }
}
