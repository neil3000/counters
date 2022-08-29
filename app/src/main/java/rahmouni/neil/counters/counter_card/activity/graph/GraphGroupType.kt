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
        R.string.graphGroupType_day_title,
        R.string.graphGroupType_day_secondary,
        ResetType.DAY,
        { cal, i -> cal.add(Calendar.DAY_OF_MONTH, -i) }),
    WEEK(
        R.string.graphGroupType_week_title,
        R.string.graphGroupType_week_secondary,
        ResetType.WEEK,
        { cal, i -> cal.add(Calendar.WEEK_OF_YEAR, -i) }),
    ALL(
        R.string.graphGroupType_all_title,
        R.string.graphGroupType_all_secondary,
        ResetType.NEVER,
        { _, _ -> });

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.formatted
    }
}