package rahmouni.neil.counters

import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class IncrementType(private val title: Int): TileDialogRadioListEnum {
    ASK_EVERY_TIME(R.string.action_askAmountEveryTime),
    VALUE(R.string.action_directlyIncreaseByValue);

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}