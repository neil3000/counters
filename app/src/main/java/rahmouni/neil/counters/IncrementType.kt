package rahmouni.neil.counters

enum class IncrementType(val title: Int, val defaultIncrementValue: Int) {
    ASK_EVERY_TIME(R.string.action_askAmountEveryTime, 1),
    VALUE(R.string.action_directlyIncreaseByValue, 1),
}