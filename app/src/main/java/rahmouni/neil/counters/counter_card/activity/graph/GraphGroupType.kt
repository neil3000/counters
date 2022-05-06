package rahmouni.neil.counters.counter_card.activity.graph

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class GraphGroupType(private val title: Int, private val formatted: Int): TileDialogRadioListEnum {
    DAY(R.string.text_day, R.string.text_groupedByDay),
    WEEK(R.string.text_week, R.string.text_groupedByWeek),
    ALL(R.string.text_dontGroup, R.string.text_notGrouped);

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.formatted
    }
}