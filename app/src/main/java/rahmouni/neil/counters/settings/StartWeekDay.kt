package rahmouni.neil.counters.settings

import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class StartWeekDay(private val title: Int, val groupQuery: String?, val calendar: Int?) : TileDialogRadioListEnum {
    LOCALE(R.string.text_localeDefault, null, null),
    MONDAY(R.string.text_monday, "1", 2),
    SUNDAY(R.string.text_sunday, "0", 1);

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}