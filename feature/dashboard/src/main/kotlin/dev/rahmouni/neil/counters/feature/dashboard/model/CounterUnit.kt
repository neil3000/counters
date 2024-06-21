package dev.rahmouni.neil.counters.feature.dashboard.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.feature.dashboard.R
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

typealias DisplayType = Pair<Double, String>

val prefixMap = mapOf(
    -30L to "q", -27L to "r", -24L to "y", -21L to "z",
    -18L to "a", -15L to "f", -12L to "p", -9L to "n",
    -6L to "μ", -3L to "m", -2L to "c", -1L to "d",
    0L to "", 1L to "da", 2L to "h", 3L to "k",
    6L to "M", 9L to "G", 12L to "T", 15L to "P",
    18L to "E", 21L to "Z", 24L to "Y", 27L to "R",
    30L to "Q",
)

data class CounterUnit(
    val unitName: String,
    val alignment: Alignment.Vertical,
    val base: Double? = null,
    val subs: List<Pair<Double, String>> = emptyList(),
    val conversions: MutableList<Conversion> = mutableListOf(),
)

data class Conversion(
    val factor: Double,
    val toUnit: () -> CounterUnit,
)

object UnitRepository {
    private val units = mutableMapOf<String, CounterUnit>()

    fun addUnit(name: String, unit: CounterUnit) {
        units[name] = unit
    }

    fun findUnit(name: String?): CounterUnit? = units[name]

    fun addConversion(fromUnit: String, factor: Double, toUnit: String) {
        findUnit(fromUnit)?.conversions?.add(Conversion(factor) { findUnit(toUnit)!! })
    }
}

@Composable
fun InitializeUnits() {
    val units = listOf(
        "LITER" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_liter),
            alignment = Alignment.Bottom,
            base = 10.0,
            subs = listOf(
                Pair(0.0, stringResource(R.string.feature_dashboard_counterUnit_liter_short)),
            ),
        ),
        "GRAM" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_gram),
            alignment = Alignment.Bottom,
            base = 10.0,
            subs = listOf(
                Pair(0.0, stringResource(R.string.feature_dashboard_counterUnit_gram_short)),
                Pair(3.0, stringResource(R.string.feature_dashboard_counterUnit_ton_short)),
            ),
        ),
        "OUNCE" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_ounce),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(0.0, stringResource(R.string.feature_dashboard_counterUnit_ounce_short)),
                Pair(16.0, stringResource(R.string.feature_dashboard_counterUnit_pound_short)),
                Pair(2000.0, stringResource(R.string.feature_dashboard_counterUnit_shortTon_short)),
                Pair(1.12, stringResource(R.string.feature_dashboard_counterUnit_longTon_short)),
            ),
        ),
        "INCH" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_inch),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(0.0, stringResource(R.string.feature_dashboard_counterUnit_inch_short)),
                Pair(12.0, stringResource(R.string.feature_dashboard_counterUnit_foot_short)),
                Pair(3.0, stringResource(R.string.feature_dashboard_counterUnit_yard_short)),
                Pair(
                    1760.0065617,
                    stringResource(R.string.feature_dashboard_counterUnit_mile_short),
                ),
            ),
        ),
        "METER" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_meter),
            alignment = Alignment.Bottom,
            base = 10.0,
            subs = listOf(
                Pair(0.0, stringResource(R.string.feature_dashboard_counterUnit_meter_short)),
            ),
        ),
        "SQUARE_METER" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_squareMeter),
            alignment = Alignment.Bottom,
            base = 100.0,
            subs = listOf(
                Pair(0.0, stringResource(R.string.feature_dashboard_counterUnit_squareMeter_short)),
            ),
        ),
        "SQUARE_INCH" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_squareInch),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(
                    0.0,
                    stringResource(R.string.feature_dashboard_counterUnit_squareInch_short),
                ),
                Pair(
                    144.0,
                    stringResource(R.string.feature_dashboard_counterUnit_squareFoot_short),
                ),
                Pair(9.0, stringResource(R.string.feature_dashboard_counterUnit_squareYard_short)),
                Pair(4840.0, stringResource(R.string.feature_dashboard_counterUnit_acre_short)),
                Pair(
                    640.0,
                    stringResource(R.string.feature_dashboard_counterUnit_squareMile_short),
                ),
            ),
        ),
        "CUBIC_METER" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_cubicMeter),
            alignment = Alignment.Bottom,
            base = 1000.0,
            subs = listOf(
                Pair(0.0, stringResource(R.string.feature_dashboard_counterUnit_cubicMeter_short)),
            ),
        ),
        "CUBIC_INCH" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_cubicInch),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(
                    0.0,
                    stringResource(R.string.feature_dashboard_counterUnit_cubicInch_short),
                ),
                Pair(
                    1728.0,
                    stringResource(R.string.feature_dashboard_counterUnit_cubicFoot_short),
                ),
                Pair(27.0, stringResource(R.string.feature_dashboard_counterUnit_cubicYard_short)),
                Pair(
                    5451773612.4,
                    stringResource(R.string.feature_dashboard_counterUnit_cubicMile_short),
                ),
            ),
        ),
        "US_GALLON" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_usGallon),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(
                    0.0,
                    stringResource(R.string.feature_dashboard_counterUnit_teaspoon_short),
                ),
                Pair(
                    3.0,
                    stringResource(R.string.feature_dashboard_counterUnit_tablespoon_short),
                ),
                Pair(
                    2.0,
                    stringResource(R.string.feature_dashboard_counterUnit_fluidOunce_short),
                ),
                Pair(8.0, stringResource(R.string.feature_dashboard_counterUnit_cup_short)),
                Pair(2.0, stringResource(R.string.feature_dashboard_counterUnit_pint_short)),
                Pair(
                    2.0,
                    stringResource(R.string.feature_dashboard_counterUnit_quart_short),
                ),
                Pair(
                    4.0,
                    stringResource(R.string.feature_dashboard_counterUnit_gallon_short),
                ),
            ),
        ),
        "IMPERIAL_GALLON" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_imperialGallon),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(
                    0.0,
                    stringResource(R.string.feature_dashboard_counterUnit_teaspoon_short),
                ),
                Pair(
                    3.0,
                    stringResource(R.string.feature_dashboard_counterUnit_tablespoon_short),
                ),
                Pair(
                    1.6,
                    stringResource(R.string.feature_dashboard_counterUnit_fluidOunce_short),
                ),
                Pair(
                    20.0,
                    stringResource(R.string.feature_dashboard_counterUnit_pint_short),
                ),
                Pair(
                    2.0,
                    stringResource(R.string.feature_dashboard_counterUnit_quart_short),
                ),
                Pair(
                    4.0,
                    stringResource(R.string.feature_dashboard_counterUnit_gallon_short),
                ),
            ),
        ),
        "SECONDS" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_second),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(
                    0.0,
                    stringResource(R.string.feature_dashboard_counterUnit_second_short),
                ),
                Pair(
                    60.0,
                    stringResource(R.string.feature_dashboard_counterUnit_time_minute_short),
                ),
                Pair(60.0, stringResource(R.string.feature_dashboard_counterUnit_time_hour_short)),
                Pair(24.0, stringResource(R.string.feature_dashboard_counterUnit_time_day_short)),
                Pair(
                    30.4167,
                    stringResource(R.string.feature_dashboard_counterUnit_time_month_short),
                ),
                Pair(12.0, stringResource(R.string.feature_dashboard_counterUnit_time_year_short)),
            ),
        ),
        "BYTE" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_byte),
            alignment = Alignment.Bottom,
            base = 10.0,
            subs = listOf(
                Pair(0.0, stringResource(R.string.feature_dashboard_counterUnit_byte_short)),
            ),
        ),
        "CELSIUS" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_celsius),
            alignment = Alignment.Top,
            base = null,
            subs = listOf(
                Pair(0.0, stringResource(R.string.feature_dashboard_counterUnit_celsius_short)),
            ),
        ),
        "FAHRENHEIT" to CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_fahrenheit),
            alignment = Alignment.Top,
            subs = listOf(
                Pair(0.0, stringResource(R.string.feature_dashboard_counterUnit_fahrenheit_short)),
            ),
        ),
    )

    units.forEach { (name, unit) -> UnitRepository.addUnit(name, unit) }

    UnitRepository.addConversion("INCH", 0.0254, "METER")
    UnitRepository.addConversion("METER", 39.37007874, "INCH")
    UnitRepository.addConversion("SQUARE_METER", 1550.0031, "SQUARE_INCH")
    UnitRepository.addConversion("SQUARE_INCH", 0.00064516, "SQUARE_METER")
}

fun getDisplayUnit(unit: CounterUnit, prefix: Long): Pair<String, Long> {
    val closestPrefix = prefixMap.keys.minByOrNull { abs(it - prefix) } ?: 0L
    val unitStr = unit.subs.find { it.first.toLong() == closestPrefix }?.second
        ?: "${prefixMap[closestPrefix] ?: ""}${unit.subs[0].second}"
    val realPrefix = abs(closestPrefix - prefix)

    return Pair(unitStr, realPrefix)
}

private fun calculateDisplayValues(
    currentValue: Double,
    unit: CounterUnit,
    base: Double,
    prefix: Long?
): DisplayType {
    val logOrder = (log10(currentValue) / log10(base)).toInt()
    val adjustedPrefix = (prefix ?: 0L) + logOrder
    val closestPrefix = prefixMap.keys.filter { it <= adjustedPrefix }.maxOrNull() ?: 0L
    val divider = base.pow((closestPrefix).toDouble())
    val unitStr = unit.subs.find { it.first.toLong() == closestPrefix }?.second
        ?: "${prefixMap[closestPrefix] ?: ""}${unit.subs[0].second}"

    return Pair(divider, unitStr)
}

fun getDisplayData(
    unit: CounterUnit,
    prefix: Long? = null,
    currentValue: Double,
    separatedUnits: Boolean = false,
): List<DisplayType> {
    val results = mutableListOf<DisplayType>()
    var remainingValue = currentValue

    fun addResult(value: Double, unitString: String) {
        results.add(Pair(value, unitString))
    }

    fun processUnitsWithBase(base: Double) {
        if (separatedUnits) {
            while (remainingValue >= 1) {
                val (divider, unitStr) = calculateDisplayValues(
                    remainingValue,
                    unit,
                    base,
                    prefix,
                )
                if (remainingValue > 0) addResult((remainingValue / divider).toInt().toDouble(), unitStr)
                remainingValue %= divider
            }
        } else {
            val (divider, unitStr) = calculateDisplayValues(
                currentValue,
                unit,
                base,
                prefix,
            )
            addResult(currentValue / divider, unitStr)
        }
    }

    fun processUnitsWithoutBase() {
        var unitStr = unit.subs[0].second

            for ((key, value) in unit.subs) {
                if (remainingValue < key) break
                if (separatedUnits) {
                    if (remainingValue > 0) addResult((remainingValue / key).toInt().toDouble(), value)
                    remainingValue %= key
                } else {
                    remainingValue /= key
                    unitStr = value
                }
            }
            results.reverse()
        addResult(remainingValue, unitStr)
    }

    unit.base?.let { base -> processUnitsWithBase(base) } ?: processUnitsWithoutBase()

    return results
}