package rahmouni.neil.counters

import android.annotation.SuppressLint
import android.content.Context
import rahmouni.neil.counters.settings.WeekDisplay
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum
import java.text.SimpleDateFormat
import java.util.*

enum class ResetType(
    private val title: Int,
    private val formatted: Int,
    val entriesGroup1: String?,
    val entriesGroup2: String?,
    val headerTitle: Int,
    val format: (Calendar, Context) -> String?
): TileDialogRadioListEnum {
    NEVER(
        R.string.text_never,
        R.string.text_never_resets,
        null,
        null,
        -1,
        { _, _ -> null }
    ),

    @SuppressLint("SimpleDateFormat")
    DAY(
        R.string.text_everyDay,
        R.string.text_resetsEveryDay,
        "start of day",
        "start of day",
        R.string.text_today,
        { d, _ ->
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
        R.string.text_resetsEveryWeek,
        "weekday %d",
        "-7 days",
        R.string.text_thisWeek,
        { d, context ->
            val cal = Calendar.getInstance()
            cal.set(
                Calendar.DAY_OF_WEEK,
                prefs.startWeekDay.calendar ?: (Calendar.getInstance().firstDayOfWeek - 1)
            )
            cal.add(Calendar.WEEK_OF_MONTH, -1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            if (d.time.before(cal.time)) {
                when (prefs.weekDisplay) {
                    WeekDisplay.FIRST_DAY -> SimpleDateFormat("MMMM d").format(d.time)
                    else -> context.getString(R.string.text_weekX, d.get(Calendar.WEEK_OF_YEAR))
                }
            } else null
        }
    ),

    @SuppressLint("SimpleDateFormat")
    MONTH(
        R.string.text_everyMonth,
        R.string.text_resetsEveryMonth,
        "start of month",
        "start of month",
        R.string.text_thisMonth,
        { d, _ ->
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
    );

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.formatted
    }
}