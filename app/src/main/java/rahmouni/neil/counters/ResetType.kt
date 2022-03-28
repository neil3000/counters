package rahmouni.neil.counters

enum class ResetType(val title: Int, val formatted: Int) {
    NEVER(R.string.text_never, R.string.text_never_resets),
    DAY(R.string.text_everyDay, R.string.text_resetsEveryDayToX),
    WEEK(R.string.text_everyWeek, R.string.text_resetsEveryWeekToX),
    MONTH(R.string.text_everyMonth, R.string.text_resetsEveryMonthToX),
}