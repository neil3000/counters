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

package dev.rahmouni.neil.counters.core.designsystem

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

data class Rn3PaddingValues(val start: Dp, val end: Dp, val top: Dp, val bottom: Dp) {
    constructor(horizontal: Dp, vertical: Dp) : this(
        start = horizontal,
        end = horizontal,
        top = vertical,
        bottom = vertical,
    )
}

fun Modifier.padding(paddingValues: Rn3PaddingValues) = padding(
    start = paddingValues.start,
    end = paddingValues.end,
    top = paddingValues.top,
    bottom = paddingValues.bottom,
)
