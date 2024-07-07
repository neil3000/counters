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
