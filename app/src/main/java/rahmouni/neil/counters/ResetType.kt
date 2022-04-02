package rahmouni.neil.counters

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.WeekFields
import java.util.*

enum class ResetType(
    val title: Int,
    val formatted: Int,
    val entriesGroup1: String?,
    val entriesGroup2: String?,
    val headerTitle: Int,
    val headerFormat: String,
    val format: (LocalDate) -> String?
) {
    NEVER(
        R.string.text_never,
        R.string.text_never_resets,
        null,
        null,
        -1,
        "",
        { null }
    ),
    DAY(
        R.string.text_everyDay,
        R.string.text_resetsEveryDayToX,
        "start of day",
        "start of day",
        R.string.text_today,
        "MMM d",
        {
            if (it != LocalDate.now())
                it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
                    .removeSuffix(it.year.toString())
            else null
        }
    ),
    WEEK(
        R.string.text_everyWeek,
        R.string.text_resetsEveryWeekToX,
        "weekday 0", //TODO
        "-7 days",
        R.string.text_thisWeek,
        "MMM d",
        {
            if (it.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) != LocalDate.now()
                    .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
            )
                it.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()).toString()
            else null
        }
    ),
    MONTH(
        R.string.text_everyMonth,
        R.string.text_resetsEveryMonthToX,
        "start of month",
        "start of month",
        R.string.text_thisMonth,
        "MMMM",
        {
            if (it.isBefore(LocalDate.now().withDayOfMonth(1)))
                it.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()).replaceFirstChar { it.uppercase() }
            else null
        }
    )
}