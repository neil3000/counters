package rahmouni.neil.counters

enum class IncrementValueType(val title: Int, val description: Int?, val hasValue: Boolean) {
    PREVIOUS(R.string.text_sameAsPreviousEntry_short, R.string.text_sameAsPreviousEntry, false),
    VALUE(R.string.text_fixedValue_short, null, true),
}