package rahmouni.neil.counters.counter_card.activity

import rahmouni.neil.counters.R
import rahmouni.neil.counters.goals.ResetType

enum class GroupType(val title: Int, val resetType: ResetType) {
    DAY(R.string.groupType_day_title, ResetType.DAY),
    WEEK(R.string.groupType_week_title, ResetType.WEEK),
    MONTH(R.string.groupType_month_title, ResetType.MONTH)
}