/*
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.feature.dashboard.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.data.model.CounterRawData
import dev.rahmouni.neil.counters.feature.dashboard.R

internal data class CounterEntity(
    val uid: String,
    val currentValue: Double,
    private val color: String,
    private val title: String?,
    private val unit: CounterUnit?,
    private val prefix: Long?,
    private val fixedUnit: Boolean = false,
    private var displayUnit: String? = null,
) {
    fun getDisplayData(): List<Pair<Double, String?>> {
        return when {
            unit == null -> listOf(Pair(currentValue, ""))
            fixedUnit -> {
                if (prefix != null && displayUnit == null) {
                    val (unitReceived, _) = getDisplayUnit(unit, prefix)
                    displayUnit = unitReceived
                }
                listOf(Pair(currentValue, displayUnit))
            }
            else -> getDisplayData(unit, prefix, currentValue)
        }
    }

    fun getAlignment(): Alignment.Vertical {
        return unit?.alignment ?: Alignment.Bottom
    }

    @Composable
    fun getColor(): Color {
        return with(MaterialTheme.colorScheme) {
            when (color) {
                "PRIMARY" -> primaryContainer
                "SECONDARY" -> secondaryContainer
                "TERTIARY" -> tertiaryContainer
                "SURFACE" -> surfaceContainer
                else -> secondaryContainer
            }
        }
    }

    @Composable
    fun getTitle(): String =
        title ?: stringResource(R.string.feature_dashboard_counterEntity_defaultTitle)
}

internal fun CounterRawData.toEntity(): CounterEntity {
    if (uid == null) throw IllegalStateException("Attempted to convert a CounterRawData with null uid to a CounterEntity")

    return CounterEntity(
        uid = uid!!,
        currentValue = currentValue.toDouble(),
        color = color,
        title = title,
        unit = UnitRepository.findUnit(unit),
        prefix = prefix,
    )
}
