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
    val shortName: String,
    val alignment: Alignment.Vertical,
    val base: Double? = null,
    val subs: List<Pair<Double, String>> = emptyList(),
    val conversions: MutableList<Conversion> = mutableListOf(),
)

data class Conversion(
    val factor: Double,
    val toUnit: () -> CounterUnit,
)

@Composable
fun InitializeUnits() {
    UnitRepository.addUnit(
        "LITER",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_liter),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_liter_short),
            alignment = Alignment.Bottom,
            base = 10.0,
        ),
    )

    UnitRepository.addUnit(
        "GRAM",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_gram),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_gram_short),
            alignment = Alignment.Bottom,
            base = 10.0,
            subs = listOf(
                Pair(3.0, "ton"),
            ),
        ),
    )

    UnitRepository.addUnit(
        "OUNCE",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_ounce),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_ounce_short),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(16.0, stringResource(R.string.feature_dashboard_counterUnit_pound_short)),
                Pair(2000.0, stringResource(R.string.feature_dashboard_counterUnit_shortTon_short)),
                Pair(1.12, stringResource(R.string.feature_dashboard_counterUnit_longTon_short)),
            ),
        ),
    )

    UnitRepository.addUnit(
        "INCH",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_inch),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_inch_short),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(12.0, stringResource(R.string.feature_dashboard_counterUnit_foot_short)),
                Pair(3.0, stringResource(R.string.feature_dashboard_counterUnit_yard_short)),
                Pair(1760.0065617, stringResource(R.string.feature_dashboard_counterUnit_mile_short)),
            ),
        ),
    )

    UnitRepository.addUnit(
        "METER",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_meter),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_meter_short),
            alignment = Alignment.Bottom,
            base = 10.0,
        ),
    )

    UnitRepository.addUnit(
        "SQUARE_METER",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_squareMeter),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_squareMeter_short),
            alignment = Alignment.Bottom,
            base = 100.0,
        ),
    )

    UnitRepository.addUnit(
        "SQUARE_INCH",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_squareInch),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_squareInch_short),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(144.0, stringResource(R.string.feature_dashboard_counterUnit_squareFoot_short)),
                Pair(9.0, stringResource(R.string.feature_dashboard_counterUnit_squareYard_short)),
                Pair(4840.0, stringResource(R.string.feature_dashboard_counterUnit_acre_short)),
                Pair(640.0, stringResource(R.string.feature_dashboard_counterUnit_squareMile_short)),
            ),
        ),
    )


    UnitRepository.addUnit(
        "CUBIC_METER",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_cubicMeters),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_cubicMeter_short),
            alignment = Alignment.Bottom,
            base = 1000.0,
        ),
    )

    UnitRepository.addUnit(
        "CUBIC_INCH",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_cubicInch),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_cubicInch_short),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(1728.0, stringResource(R.string.feature_dashboard_counterUnit_cubicFoot_short)),
                Pair(27.0, stringResource(R.string.feature_dashboard_counterUnit_cubicYard_short)),
                Pair(5451773612.4, stringResource(R.string.feature_dashboard_counterUnit_cubicMile_short)),
            ),
        ),
    )

    UnitRepository.addUnit(
        "US_GALLON",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_usGallon),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_usGallon_short),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(1.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_teaspoon_short)),
                Pair(3.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_tablespoon_short)),
                Pair(2.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_fluidOunce_short)),
                Pair(8.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_cup_short)),
                Pair(2.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_pint_short)),
                Pair(2.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_quart_short)),
                Pair(4.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_gallon_short)),
            ),
        ),
    )

    UnitRepository.addUnit(
        "IMPERIAL_GALLON",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_imperialGallon),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_imperialGallon_short),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(1.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_teaspoon_short)),
                Pair(3.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_tablespoon_short)),
                Pair(1.6, stringResource(R.string.feature_dashboard_counterUnit_gallon_fluidOunce_short)),
                Pair(20.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_pint_short)),
                Pair(2.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_quart_short)),
                Pair(4.0, stringResource(R.string.feature_dashboard_counterUnit_gallon_gallon_short)),
            ),
        ),
    )

    UnitRepository.addUnit(
        "SECONDS",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_second),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_second_short),
            alignment = Alignment.Bottom,
            base = null,
            subs = listOf(
                Pair(60.0, stringResource(R.string.feature_dashboard_counterUnit_time_minute_short)),
                Pair(60.0, stringResource(R.string.feature_dashboard_counterUnit_time_hour_short)),
                Pair(24.0, stringResource(R.string.feature_dashboard_counterUnit_time_day_short)),
                Pair(30.4167, stringResource(R.string.feature_dashboard_counterUnit_time_month_short)),
                Pair(12.0, stringResource(R.string.feature_dashboard_counterUnit_time_year_short)),
            ),
        ),
    )

    UnitRepository.addUnit(
        "BYTE",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_byte),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_byte_short),
            alignment = Alignment.Bottom,
            base = 10.0,
        ),
    )

    UnitRepository.addUnit(
        "CELSIUS",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_celsius),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_celsius_short),
            alignment = Alignment.Top,
            base = null,
        ),
    )

    UnitRepository.addUnit(
        "FAHRENHEIT",
        CounterUnit(
            unitName = stringResource(R.string.feature_dashboard_counterUnit_fahrenheit),
            shortName = stringResource(R.string.feature_dashboard_counterUnit_fahrenheit_short),
            alignment = Alignment.Top,
        ),
    )

    initializeConversions()
}

private fun initializeConversions() {
    val inch = UnitRepository.findUnitByVariableName("INCH")
    val meter = UnitRepository.findUnitByVariableName("METER")
    inch?.conversions?.add(Conversion(0.0254) { meter!! })
    meter?.conversions?.add(Conversion(39.37007874) { inch!! })
}

object UnitRepository {
    private val units: MutableMap<String, CounterUnit> = mutableMapOf()

    fun addUnit(variableName: String, unit: CounterUnit) {
        units[variableName] = unit
    }

    fun findUnitByVariableName(variableName: String?): CounterUnit? {
        return units[variableName]
    }
}

fun getDisplayUnit(unit: CounterUnit, prefix: Long): Pair<String, Long> {
    val closestPrefix = prefixMap.keys.minByOrNull { abs(it - prefix) } ?: 0L
    val unitStr = unit.subs.find { it.first.toLong() == closestPrefix }?.second
        ?: "${prefixMap[closestPrefix] ?: ""}${unit.shortName}"
    val realPrefix = abs(closestPrefix - prefix)

    return Pair(unitStr, realPrefix)
}

private fun calculateDisplayValues(
    currentValue: Double,
    unit: CounterUnit,
    base: Double,
    prefix: Long?,
): DisplayType {
    val order = (log10(currentValue) / log10(base)).toLong()
    val adjustedPrefix = (prefix ?: 0) + order
    val (unitStr, realPrefix) = getDisplayUnit(unit, adjustedPrefix)
    val divider = base.pow((order - realPrefix).toDouble())

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
        if (value > 0) results.add(Pair(value, unitString))
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
                addResult((remainingValue / divider).toInt().toDouble(), unitStr)
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
        var unitStr = unit.shortName

        if (unit.subs.isNotEmpty()) {
            for ((key, value) in unit.subs) {
                if (remainingValue < key) break
                if (separatedUnits) {
                    addResult((remainingValue / key).toInt().toDouble(), value)
                    remainingValue %= key
                } else {
                    remainingValue /= key
                    unitStr = value
                }
            }
            results.reverse()
        }
        addResult(remainingValue, if (separatedUnits) unit.shortName else unitStr)
    }

    unit.base?.let { base -> processUnitsWithBase(base) } ?: processUnitsWithoutBase()

    return results
}