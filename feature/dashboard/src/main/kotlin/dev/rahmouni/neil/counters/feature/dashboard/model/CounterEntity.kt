/*
 * Copyright 2024 Rahmouni Neïl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    val currentValue: Long,
    private val color: String,
    private val title: String?,
    private val unit: CounterUnit?,
    private val prefix: Long?,
) {
    fun getDisplayData(): Pair<String, String> {
        return CounterUnit.getDisplayString(unit, prefix, currentValue)
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
        currentValue = currentValue,
        color = color,
        title = title,
        unit = CounterUnit.entries.find { it.unitName == unit },
        prefix = prefix,
    )
}
