package rahmouni.neil.counters

//TODO i18n
enum class IncrementValueType(val title: String, val description: String, val hasValue: Boolean) {
    PREVIOUS("Same as previous value", "Same amount as previous entry", false),
    VALUE("Fixed value", "%s", true),
}