package rahmouni.neil.counters

enum class ResetType(val title: Int, val formatted: Int, val testMillis: Long) {
    NEVER(R.string.text_never, R.string.text_never_resets, 1000*60*24*32), // put the max of the others
    DAY(R.string.text_everyDay, R.string.text_resetsEveryDayToX, 1000*60*25),
    WEEK(R.string.text_everyWeek, R.string.text_resetsEveryWeekToX, 1000*60*24*8),
    MONTH(R.string.text_everyMonth, R.string.text_resetsEveryMonthToX, 1000*60*24*32),
}