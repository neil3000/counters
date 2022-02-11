package rahmouni.neil.counters

//TODO i18n
enum class IncrementType(val title: String, val description: String, val hasValue: Boolean) {
    LAST_TIME("Same as previous entry","Same as previous entry", false),
    ASK_EVERY_TIME("Ask every time", "Ask every time", false),
    VALUE("Increase by value","Increase by %s", true),
}