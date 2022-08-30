package rahmouni.neil.counters.counter_card.activity.health_connect

import androidx.health.connect.client.records.ExerciseRepetitionsRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class HealthConnectType(
    private val title: Int,
    val activitySessionType: String,
    val repetitionsType: String
) : TileDialogRadioListEnum {
    SQUAT(
        R.string.healthConnectType_squats_title,
        ExerciseSessionRecord.ExerciseType.SQUAT,
        ExerciseRepetitionsRecord.ExerciseType.SQUAT
    ),
    BURPEES(
        R.string.healthConnectType_burpees_title,
        ExerciseSessionRecord.ExerciseType.BURPEE,
        ExerciseRepetitionsRecord.ExerciseType.BURPEE
    ),
    CRUNCHES(
        R.string.healthConnectType_crunches_title,
        ExerciseSessionRecord.ExerciseType.CRUNCH,
        ExerciseRepetitionsRecord.ExerciseType.CRUNCH
    ),
    JUMPING_JACKS(
        R.string.healthConnectType_jumpingJacks_title,
        ExerciseSessionRecord.ExerciseType.JUMPING_JACK,
        ExerciseRepetitionsRecord.ExerciseType.JUMPING_JACK
    ),
    PLANK(
        R.string.healthConnectType_plank_title,
        ExerciseSessionRecord.ExerciseType.PLANK,
        ExerciseRepetitionsRecord.ExerciseType.PLANK
    ),
    ROPE_JUMPS(
        R.string.healthConnectType_ropeJumps_title,
        ExerciseSessionRecord.ExerciseType.JUMP_ROPE,
        ExerciseRepetitionsRecord.ExerciseType.JUMP_ROPE
    );

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}