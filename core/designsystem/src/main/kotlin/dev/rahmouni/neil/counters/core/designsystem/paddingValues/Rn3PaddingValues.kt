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
