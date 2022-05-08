package rahmouni.neil.counters.counter_card.activity.graph

import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum
import java.util.*

enum class GraphGroupType(
    private val title: Int,
    private val formatted: Int,
    val resetType: ResetType,
    val offset: (Calendar, Int) -> Unit
) : TileDialogRadioListEnum {

    DAY(
        R.string.text_day,
        R.string.text_groupedByDay,
        ResetType.DAY,
        { cal, i -> cal.add(Calendar.DAY_OF_MONTH, -i) }),
    WEEK(
        R.string.text_week,
        R.string.text_groupedByWeek,
        ResetType.WEEK,
        { cal, i -> cal.add(Calendar.WEEK_OF_YEAR, -i) }),
    ALL(R.string.text_dontGroup, R.string.text_notGrouped, ResetType.NEVER, { _, _ -> });

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.formatted
    }
}