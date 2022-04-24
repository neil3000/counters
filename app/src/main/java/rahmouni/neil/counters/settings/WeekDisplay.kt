package rahmouni.neil.counters.settings

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class WeekDisplay(private val title: Int) : TileDialogRadioListEnum {
    NUMBER(R.string.text_weekNumberInYear), FIRST_DAY(R.string.text_firstDayOfTheWeek);

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}