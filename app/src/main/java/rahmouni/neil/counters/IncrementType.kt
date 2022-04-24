package rahmouni.neil.counters

import rahmouni.neil.counters.utils.tiles.TileDialogRadioListEnum

enum class IncrementType(private val title: Int, val defaultIncrementValue: Int): TileDialogRadioListEnum {
    ASK_EVERY_TIME(R.string.action_askAmountEveryTime, 1),
    VALUE(R.string.action_directlyIncreaseByValue, 1);

    override fun title(): Int {
        return this.title
    }

    override fun formatted(): Int {
        return this.title
    }
}