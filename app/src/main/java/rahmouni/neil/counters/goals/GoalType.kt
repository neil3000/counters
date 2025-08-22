package rahmouni.neil.counters.goals

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class GoalType(
    private val title: Int,
    private val formatted: Int,
) : TileDialogRadioListEnum {
    TIME_PERIOD(
        R.string.goalType_timePeriod_title,
        R.string.goalType_timePeriod_secondary,
    ),
    ENTRY(
        R.string.goalType_entry_title,
        R.string.goalType_entry_secondary,
    );

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.formatted
    }
}