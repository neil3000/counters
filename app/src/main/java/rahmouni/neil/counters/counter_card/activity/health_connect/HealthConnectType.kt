package rahmouni.neil.counters.counter_card.activity.health_connect

import androidx.health.connect.client.records.ActivitySession
import androidx.health.connect.client.records.Repetitions
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class HealthConnectType(
    private val title: Int,
    val activitySessionType: String,
    val repetitionsType: String
) : TileDialogRadioListEnum {
    SQUAT(R.string.text_squats, ActivitySession.ActivityType.SQUAT, Repetitions.ActivityType.SQUAT),
    BURPEES(
        R.string.text_burpees,
        ActivitySession.ActivityType.BURPEE,
        Repetitions.ActivityType.BURPEE
    ),
    CRUNCHES(
        R.string.text_crunches,
        ActivitySession.ActivityType.CRUNCH,
        Repetitions.ActivityType.CRUNCH
    ),
    JUMPING_JACKS(
        R.string.text_jumpingJacks,
        ActivitySession.ActivityType.JUMPING_JACK,
        Repetitions.ActivityType.JUMPING_JACK
    ),
    PLANK(R.string.text_plank, ActivitySession.ActivityType.PLANK, Repetitions.ActivityType.PLANK),
    ROPE_JUMPS(
        R.string.text_ropeJumps,
        ActivitySession.ActivityType.JUMP_ROPE,
        Repetitions.ActivityType.JUMP_ROPE
    );

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}