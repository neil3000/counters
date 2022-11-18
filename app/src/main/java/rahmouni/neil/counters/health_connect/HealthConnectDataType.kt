package rahmouni.neil.counters.health_connect

import androidx.health.connect.client.records.Record
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum
import java.time.ZonedDateTime

enum class HealthConnectDataType(
    private val title: Int,
    val createRecord: (start: ZonedDateTime, end: ZonedDateTime, count: Long, type: String) -> Record
) : TileDialogRadioListEnum {
    REPETITIONS(
        R.string.healthConnectDataType_repetitions,
        { start, end, count, type ->
            androidx.health.connect.client.records.ExerciseRepetitionsRecord(
                startTime = start.toInstant(),
                startZoneOffset = start.offset,
                endTime = end.toInstant(),
                endZoneOffset = end.offset,
                count = count,
                type = type
            )
        }
    );

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}