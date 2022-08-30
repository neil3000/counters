package rahmouni.neil.counters.settings

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class StartWeekDay(private val title: Int, val groupQuery: String?) : TileDialogRadioListEnum {
    LOCALE(R.string.startWeekDay_automatic_title, null),
    MONDAY(R.string.startWeekDay_monday_title, "0"),
    SUNDAY(R.string.startWeekDay_sunday_title, "6"),
    SATURDAY(R.string.startWeekDay_saturday_title, "5");

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}