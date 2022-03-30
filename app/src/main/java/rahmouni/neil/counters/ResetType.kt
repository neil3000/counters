package rahmouni.neil.counters

enum class ResetType(val title: Int, val formatted: Int, val entriesGroup: String?) {
    NEVER(R.string.text_never, R.string.text_never_resets, null),
    DAY(R.string.text_everyDay, R.string.text_resetsEveryDayToX, "'start of day'"),
    WEEK(R.string.text_everyWeek, R.string.text_resetsEveryWeekToX, "'weekday 0', '-7 days'"),
    MONTH(R.string.text_everyMonth, R.string.text_resetsEveryMonthToX, "'start of month'"),
}