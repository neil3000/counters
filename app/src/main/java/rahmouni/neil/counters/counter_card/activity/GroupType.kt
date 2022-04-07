package rahmouni.neil.counters.counter_card.activity

import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType

enum class GroupType(val title: Int, val resetType: ResetType) {
    DAY(R.string.text_day, ResetType.DAY),
    WEEK(R.string.text_week, ResetType.WEEK),
    MONTH(R.string.text_month, ResetType.MONTH)
}