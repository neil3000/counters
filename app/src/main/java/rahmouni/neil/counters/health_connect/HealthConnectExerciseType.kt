package rahmouni.neil.counters.health_connect

import androidx.health.connect.client.records.ExerciseSessionRecord
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class HealthConnectExerciseType(
    private val title: Int,
    val exerciseType: String,
    val dataTypes: Set<HealthConnectDataType>,
    val defaultDataType: HealthConnectDataType
) : TileDialogRadioListEnum {

    BACK_EXTENSION(
        R.string.healthConnectExerciseType_backExtension,
        ExerciseSessionRecord.ExerciseType.BACK_EXTENSION,
        setOf(HealthConnectDataType.REPETITIONS),
        HealthConnectDataType.REPETITIONS
    );

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}