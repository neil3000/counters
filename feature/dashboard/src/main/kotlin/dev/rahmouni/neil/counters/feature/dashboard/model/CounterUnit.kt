package dev.rahmouni.neil.counters.feature.dashboard.model

import androidx.compose.ui.Alignment
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

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
            Pair(16.0, "Pound"),
            Pair(2000.0, "short ton"),
            Pair(1.12, "long ton"),
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
        "tea spoon",
        alignment = Alignment.Bottom,
        null,
        listOf(
            Pair(3.0, "table spoon"),
            Pair(2.0, "Fluid Once"),
            Pair(8.0, "cup"),
            Pair(2.0, "pint"),
            Pair(2.0, "quart"),
            Pair(4.0, "gallon"),
        ),
    ),

    IMPERIAL_GALLONS(
        "Imperial Gallons",
        "tea spoon",
        alignment = Alignment.Bottom,
        null,
        listOf(
            Pair(3.0, "table spoon"),
            Pair(1.6, "Fluid Once"),
            Pair(20.0, "pint"),
            Pair(2.0, "quart"),
            Pair(4.0, "gallon"),
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
            val prefixStr = prefixMap[closestPrefix] ?: ""

            val unitStr = unit.subs?.find { it.first.toLong() == closestPrefix }?.second
                ?: "$prefixStr${unit.shortName}"
            val realPrefix = abs(closestPrefix - prefix)

            return Pair(unitStr, realPrefix)
        }

        fun getDisplayData(
            unit: CounterUnit,
            prefix: Long? = null,
            currentValue: Double,
            separatedUnits: Boolean = false,
        ): List<Pair<Double, String>> {
            unit.base?.let { base ->
                val order = (log10(currentValue) / log10(base)).toLong()
                val adjustedPrefix = (prefix ?: 0) + order

                val (unitStr, realPrefix) = getDisplayUnit(unit, adjustedPrefix)
                val divider = base.pow((order - realPrefix).toDouble())
                val valueStr = currentValue / divider

                return listOf(Pair(valueStr, unitStr))
            }

            //TODO: fix separated units
            //TODO: SECONDS can also be of base 10

            if (unit == INCHES || unit == SECONDS) {
                val subs = unit.subs!!
                var convertedValue = currentValue
                var unitStr = unit.shortName

                if (separatedUnits) {
                    val result = mutableListOf<Pair<Double, String>>()

                    for ((key, value) in subs) {
                        if (convertedValue >= key) {
                            convertedValue /= key
                            unitStr = value
                            result.add(Pair(convertedValue.roundToInt().toDouble(), unitStr))
                        } else {
                            break
                        }
                    }

                    if (convertedValue > 0) {
                        result.reverse()
                        result.add(Pair(convertedValue, unit.shortName))
                    }

                    return result
                } else {
                    for ((key, value) in subs) {
                        if (convertedValue >= key) {
                            convertedValue /= key
                            unitStr = value
                        } else {
                            break
                        }
                    }

                    return listOf(Pair(convertedValue, unitStr))
                }
            }

            return listOf(Pair(currentValue, unit.shortName))
        }
    }
}