package rahmouni.neil.counters.settings

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class StartWeekDay(private val title: Int, val groupQuery: String?) : TileDialogRadioListEnum {
    LOCALE(R.string.text_localeDefault, null),
    MONDAY(R.string.text_monday, "0"),
    SUNDAY(R.string.text_sunday, "6"),
    SATURDAY(R.string.text_saturday, "5");

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}