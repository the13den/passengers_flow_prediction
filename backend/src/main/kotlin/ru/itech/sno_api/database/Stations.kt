import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.itech.sno_api.dto.StationDTO

object Stations : Table("station_table") {
    private val id = long("id").autoIncrement()
    private val stationName = varchar("station_name", 255)
    private val branchName = varchar("branch_name", 255)
    private val timeStart = varchar("time_start", 255)  // Now a string
    private val timeStop = varchar("time_stop", 255)   // Now a string
    private val workload = long("workload")

    override val primaryKey = PrimaryKey(id)

    fun insert(stationDTO: StationDTO) {
        transaction {
            insert {
                it[stationName] = stationDTO.stationName
                it[branchName] = stationDTO.branchName
                it[timeStart] = stationDTO.timeStart
                it[timeStop] = stationDTO.timeStop
                it[workload] = stationDTO.workload
            }
        }
    }

    fun fetchStationByName(stationName: String): StationDTO? {
        return try {
            transaction {
                select { Stations.stationName eq stationName }.singleOrNull()?.let {
                    StationDTO(
                        id = it[Stations.id],
                        stationName = it[Stations.stationName],
                        branchName = it[branchName],
                        timeStart = it[timeStart],
                        timeStop = it[timeStop],
                        workload = it[workload]
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Assuming you need to keep this function, revise it to handle strings
    fun fetchStationsByDate(date: String): List<StationDTO> {
        return transaction {
            select { timeStart eq date }
                .map {
                    StationDTO(
                        id = it[Stations.id],
                        stationName = it[stationName],
                        branchName = it[branchName],
                        timeStart = it[timeStart],
                        timeStop = it[timeStop],
                        workload = it[workload]
                    )
                }
        }
    }

    fun fetchStationDetailed(timeStart: String,timeStop: String, name: String, branch: String): StationDTO? {
        return transaction {
            select {
                (stationName eq name) and (branchName eq branch)
            }
                .mapNotNull {
                    StationDTO(
                        id = it[Stations.id],
                        stationName = it[stationName],
                        branchName = it[branchName],
                        timeStart = it[Stations.timeStart],
                        timeStop = it[Stations.timeStop],
                        workload = it[workload]
                    )
                }.singleOrNull()
        }
    }
}
