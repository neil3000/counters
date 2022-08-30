package rahmouni.neil.counters.settings

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class WeekDisplay(private val title: Int) : TileDialogRadioListEnum {
    NUMBER(R.string.weekDisplay_number), FIRST_DAY(R.string.weekDisplay_firstDay);

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}