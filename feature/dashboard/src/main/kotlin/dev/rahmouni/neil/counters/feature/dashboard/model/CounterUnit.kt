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
    val subs: List<Pair<Double, String>>? = null,
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
        listOf(
            Pair(3.0, "ton"),
        ),
    ),
    OUNCE(
        "Ounce",
        "oz",
        alignment = Alignment.Bottom,
        null,
        listOf(
            Pair(16.0, "lb"),
            Pair(2000.0, "sh tn"),
            Pair(1.12, "lng tn"),
        ),
    ),
    INCHES(
        "Inches",
        "in",
        alignment = Alignment.Bottom,
        null,
        listOf(
            Pair(12.0, "ft"),
            Pair(3.0, "yd"),
            Pair(1760.0065617, "mi"),
        ),
    ),
    METERS(
        "Meters",
        "m",
        alignment = Alignment.Bottom,
        10.0,
    ),
    SQUARE_METERS(
        "Square Meters",
        "m²",
        alignment = Alignment.Bottom,
        100.0,
    ),
    SQUARE_INCHES(
        "Square Inches",
        "in²",
        alignment = Alignment.Bottom,
        null,
        listOf(
            Pair(144.0, "ft²"),
            Pair(9.0, "yd²"),
            Pair(4840.0, "ac"),
            Pair(640.0, "mi²"),
        ),
    ),
    CUBIC_METERS(
        "Cubic Meters",
        "m³",
        alignment = Alignment.Bottom,
        1000.0,
    ),
    US_GALLONS(
        "US Gallons",
        "gal",
        alignment = Alignment.Bottom,
        null,
        listOf(
            Pair(1.0, "tsp"),
            Pair(3.0, "tbsp"),
            Pair(2.0, "fl oz"),
            Pair(8.0, "cup"),
            Pair(2.0, "pt"),
            Pair(2.0, "qt"),
            Pair(4.0, "gal"),
        ),
    ),
    IMPERIAL_GALLONS(
        "Imperial Gallons",
        "imp gal",
        alignment = Alignment.Bottom,
        null,
        listOf(
            Pair(1.0, "tsp"),
            Pair(3.0, "tbsp"),
            Pair(1.6, "fl oz"),
            Pair(20.0, "pt"),
            Pair(2.0, "qt"),
            Pair(4.0, "gal"),
        ),
    ),
    CUBIC_INCHES(
        "Cubic Inches",
        "in³",
        alignment = Alignment.Bottom,
        null,
        listOf(
            Pair(1728.0, "ft³"),
            Pair(27.0, "yd³"),
            Pair(5451773612.4, "mi³"),
        ),
    ),
    SECONDS(
        "Seconds",
        "s",
        alignment = Alignment.Bottom,
        null,
        listOf(
            Pair(60.0, "min"),
            Pair(60.0, "h"),
            Pair(24.0, "d"),
            Pair(30.4167, "mo"),
            Pair(12.0, "yr"),
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
    ),
    FAHRENHEIT(
        "Fahrenheit",
        "°F",
        alignment = Alignment.Top,
        null,
    );

    companion object {
        fun getDisplayUnit(unit: CounterUnit, prefix: Long): Pair<String, Long> {
            val closestPrefix = prefixMap.keys.minByOrNull { abs(it - prefix) } ?: 0L
            val unitStr = unit.subs?.find { it.first.toLong() == closestPrefix }?.second
                ?: "${prefixMap[closestPrefix] ?: ""}${unit.shortName}"
            val realPrefix = abs(closestPrefix - prefix)

            return Pair(unitStr, realPrefix)
        }

        fun getDisplayData(
            unit: CounterUnit,
            prefix: Long? = null,
            currentValue: Double,
            separatedUnits: Boolean = false,
        ): List<Pair<Double, String>> {
            if (separatedUnits) {
                if (unit.base != null) {
                    val results = mutableListOf<Pair<Double, String>>()
                    var remainingValue = currentValue

                    fun addResult(value: Double, prefix: Long) {
                        if (value > 0) {
                            val (unitStr, _) = getDisplayUnit(unit, prefix)
                            results.add(Pair(value, unitStr))
                        }
                    }

                    while (remainingValue >= 1) {
                        val order = (log10(remainingValue) / log10(unit.base)).toLong()
                        val adjustedPrefix = (prefix ?: 0) + order
                        val divider = unit.base.pow(order.toDouble())
                        val valueStr = (remainingValue / divider).toInt().toDouble()
                        remainingValue %= divider

                        addResult(valueStr, adjustedPrefix)
                    }

                    addResult(remainingValue, prefix ?: 0)

                    return results
                }
                val results = mutableListOf<Pair<Double, String>>()
                var remainingValue = currentValue
                val unitStr = unit.shortName
                if (!unit.subs.isNullOrEmpty()) {
                    for ((key, value) in unit.subs) {
                        if (remainingValue < key) break
                        val wholePart = (remainingValue / key).toInt()
                        results.add(Pair(wholePart.toDouble(), value))
                        remainingValue %= key
                    }
                }

                if (remainingValue > 0) {
                    results.add(Pair(remainingValue, unitStr))
                }

                return results
            }

            if (unit.base != null) {
                val base = unit.base
                val order = (log10(currentValue) / log10(base)).toLong()
                val adjustedPrefix = (prefix ?: 0) + order
                val (unitStr, realPrefix) = getDisplayUnit(unit, adjustedPrefix)
                val divider = base.pow((order - realPrefix).toDouble())
                val valueStr = currentValue / divider
                return listOf(Pair(valueStr, unitStr))
            }

            var remainingValue = currentValue
            var unitStr = unit.shortName
            if (!unit.subs.isNullOrEmpty()) {
                for ((key, value) in unit.subs) {
                    if (remainingValue < key) break
                    remainingValue /= key
                    unitStr = value
                }
            }
            return listOf(Pair(remainingValue, unitStr))
        }
    }
}