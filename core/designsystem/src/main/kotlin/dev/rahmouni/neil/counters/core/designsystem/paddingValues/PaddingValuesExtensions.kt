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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection

fun Modifier.padding(paddingValues: Rn3PaddingValues) = padding(
    start = paddingValues.start,
    end = paddingValues.end,
    top = paddingValues.top,
    bottom = paddingValues.bottom,
)

@Composable
fun PaddingValues.toRn3PaddingValues(): Rn3PaddingValues {
    val layoutDirection = LocalLayoutDirection.current

    return Rn3PaddingValues(
        start = calculateStartPadding(layoutDirection),
        end = calculateEndPadding(layoutDirection),
        top = calculateTopPadding(),
        bottom = calculateBottomPadding(),
    )
}

@Composable
fun WindowInsets.toRn3PaddingValues(): Rn3PaddingValues = asPaddingValues().toRn3PaddingValues()