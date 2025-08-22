package rahmouni.neil.counters.goals

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class GoalResetType : TileDialogRadioListEnum {
    FollowCounter;

    override fun title(): Int {
        return R.string.counterGoalSettingsActivity_followCounter_title
    }

    override fun formatted(): Int {
        return if (FirebaseRemoteConfig.getInstance().getBoolean("i_253")) R.string.counterGoalSettingsActivity_followCounter_formatted_v2 else R.string.counterGoalSettingsActivity_followCounter_formatted
    }
}