package rahmouni.neil.counters

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

enum class ResetType(
    val title: Int,
    val formatted: Int,
    val entriesGroup1: String?,
    val entriesGroup2: String?,
    val headerTitle: Int,
    val format: (Calendar) -> String?
) {
    NEVER(
        R.string.text_never,
        R.string.text_never_resets,
        null,
        null,
        -1,
        { null }
    ),

    @SuppressLint("SimpleDateFormat")
    DAY(
        R.string.text_everyDay,
        R.string.text_resetsEveryDayToX,
        "start of day",
        "start of day",
        R.string.text_today,
        { d ->
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            if (d.time.before(cal.time))
                SimpleDateFormat("MMMM d").format(d.time)
            else null
        }
    ),

    @SuppressLint("SimpleDateFormat")
    WEEK(
        R.string.text_everyWeek,
        R.string.text_resetsEveryWeekToX,
        "weekday 1", //TODO
        "-7 days",
        R.string.text_thisWeek,
        { d ->
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_WEEK, 2)
            cal.add(Calendar.WEEK_OF_MONTH, -1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            if (d.time.before(cal.time)) {
                SimpleDateFormat("MMMM d").format(d.time)
            } else null
        }
    ),

    @SuppressLint("SimpleDateFormat")
    MONTH(
        R.string.text_everyMonth,
        R.string.text_resetsEveryMonthToX,
        "start of month",
        "start of month",
        R.string.text_thisMonth,
        { d ->
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            if (d.time.before(cal.time))
                SimpleDateFormat("MMMM").format(d.time)
            else null
        }
    )
}