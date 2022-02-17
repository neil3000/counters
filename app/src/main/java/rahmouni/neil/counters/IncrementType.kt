package rahmouni.neil.counters

//TODO i18n
enum class IncrementType(val title: String, val defaultIncrementValue: Int) {
    ASK_EVERY_TIME("Ask amount every time", 1),
    VALUE("Directly increase by value", 1),
}