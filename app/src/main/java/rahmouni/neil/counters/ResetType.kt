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
    val headerTitle: Int,
    val format: (Calendar, Context) -> String?
) : TileDialogRadioListEnum {
    NEVER(
        R.string.resetType_never_title,
        R.string.resetType_never_secondary,
        null,
        -1,
        { _, _ -> null }
    ),

    @SuppressLint("SimpleDateFormat")
    DAY(
        R.string.resetType_day_title,
        R.string.resetType_day_secondary,
        "start of day",
        R.string.resetType_day_header_title,
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
        R.string.resetType_week_title,
        R.string.resetType_week_secondary,
        "weekday %d",
        R.string.resetType_week_header_title,
        { d, context ->
            val cal = Calendar.getInstance()
            cal.set(
                Calendar.DAY_OF_WEEK,
                (Calendar.getInstance().firstDayOfWeek - 2) % 7
            )
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            if (d.time.before(cal.time)) {
                when (prefs.weekDisplay) {
                    WeekDisplay.FIRST_DAY -> SimpleDateFormat("MMMM d").format(d.time)
                    else -> context.getString(
                        R.string.resetType_weekX_header_title,
                        d.get(Calendar.WEEK_OF_YEAR)
                    )
                }
            } else null
        }
    ),

    @SuppressLint("SimpleDateFormat")
    MONTH(
        R.string.resetType_month_title,
        R.string.resetType_month_secondary,
        "start of month",
        R.string.resetType_month_header_title,
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