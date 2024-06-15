package dev.rahmouni.neil.counters.feature.dashboard.model

import androidx.compose.ui.Alignment
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

val prefixMap = mapOf(
    -30L to "q", -27L to "r", -24L to "y", -21L to "z",
    -18L to "a", -15L to "f", -12L to "p", -9L to "n",
    -6L to "μ", -3L to "m", -2L to "c", -1L to "d",
    0L to "", 1L to "da", 2L to "h", 3L to "k",
    6L to "M", 9L to "G", 12L to "T", 15L to "P",
    18L to "E", 21L to "Z", 24L to "Y", 27L to "R",
    30L to "Q",
)

enum class CounterUnit(
    val unitName: String,
    val shortName: String,
    val alignment: Alignment.Vertical,
    val base: Double? = null,
    val subs: Map<Long, String>? = null,
) {
    LITERS(
        "Liters",
        "L",
        alignment = Alignment.Bottom,
        10.0,
    ),
    GRAMS(
        "Grams",
        "g",
        alignment = Alignment.Bottom,
        10.0,
        mapOf(
            3L to "ton",
        ),
    ),
    INCHES(
        "Inches",
        "in",
        alignment = Alignment.Bottom,
        null,
        mapOf(
            12L to "ft",
            36L to "yd",
        ),
    ),
    METERS(
        "Meters",
        "m",
        alignment = Alignment.Bottom,
        10.0,
    ),
    SECONDS(
        "Seconds",
        "s",
        alignment = Alignment.Bottom,
        60.0,
        mapOf(
            1L to "min",
            2L to "h",
        ),
    ),
    BYTES(
        "Bytes",
        "B",
        alignment = Alignment.Bottom,
        10.0,
    ),
    CELSIUS(
        "Celsius",
        "°C",
        alignment = Alignment.Top,
        null,
    );

    companion object {
        fun getDisplayUnit(unit: CounterUnit, prefix: Long): Pair<String, Long> {
            val closestPrefix = prefixMap.keys.minByOrNull { abs(it - prefix) } ?: 0L
            val prefixStr = prefixMap[closestPrefix] ?: ""
            val unitStr = unit.subs?.get(closestPrefix) ?: "$prefixStr${unit.shortName}"
            val realPrefix = abs(closestPrefix - prefix)

            return Pair(unitStr, realPrefix)
        }

        fun getDisplayData(
            unit: CounterUnit,
            prefix: Long? = null,
            currentValue: Double,
        ): Pair<Double, String> {
            unit.base?.let { base ->
                val order = (log10(currentValue) / log10(base)).toLong()
                val adjustedPrefix = (prefix ?: 0) + order

                val (unitStr, realPrefix) = getDisplayUnit(unit, adjustedPrefix)
                val divider = base.pow((order - realPrefix).toDouble())
                val valueStr = currentValue / divider

                return Pair(valueStr, unitStr)
            }

            if (unit == INCHES) {
                val subs = unit.subs!!
                val closestSub = subs.keys.filter { it <= currentValue }.maxOrNull() ?: 1L
                val unitStr = subs[closestSub] ?: unit.shortName
                val valueStr = currentValue / closestSub

                return Pair(valueStr, unitStr)
            }

            return Pair(currentValue, unit.shortName)
        }
    }
}