/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.core.designsystem.paddingValues

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Rn3PaddingValues(
    val start: Dp = 0.dp,
    val end: Dp = 0.dp,
    val top: Dp = 0.dp,
    val bottom: Dp = 0.dp,
) {
    constructor(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) : this(
        start = horizontal,
        end = horizontal,
        top = vertical,
        bottom = vertical,
    )

    operator fun plus(paddingValues: Rn3PaddingValues): Rn3PaddingValues = Rn3PaddingValues(
        start + paddingValues.start,
        end + paddingValues.end,
        top + paddingValues.top,
        bottom + paddingValues.bottom,
    )

    operator fun times(paddingValues: Rn3PaddingValues): Rn3PaddingValues = Rn3PaddingValues(
        start * paddingValues.start.value,
        end * paddingValues.end.value,
        top * paddingValues.top.value,
        bottom * paddingValues.bottom.value,
    )

    fun only(direction: Rn3PaddingValues): Rn3PaddingValues = this * direction

    fun toComposePaddingValues(): PaddingValues {
        return PaddingValues(start, top, end, bottom)
    }

    fun add(
        start: Dp = 0.dp,
        end: Dp = 0.dp,
        top: Dp = 0.dp,
        bottom: Dp = 0.dp,
    ): Rn3PaddingValues = plus(Rn3PaddingValues(start, end, top, bottom))

    fun add(horizontal: Dp = 0.dp, vertical: Dp = 0.dp): Rn3PaddingValues =
        plus(Rn3PaddingValues(horizontal, vertical))
}
