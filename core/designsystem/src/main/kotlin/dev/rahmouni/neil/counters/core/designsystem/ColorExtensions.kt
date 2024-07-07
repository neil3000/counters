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

package dev.rahmouni.neil.counters.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable

val ColorScheme.rn3ErrorContainer
    @Composable
    get() = if (isSystemInDarkTheme()) errorContainer else error

val ColorScheme.rn3Error
    @Composable
    get() = contentColorFor(rn3ErrorContainer)

@Composable
fun ButtonDefaults.rn3ErrorButtonColors() = buttonColors().copy(
    containerColor = MaterialTheme.colorScheme.rn3ErrorContainer,
    contentColor = MaterialTheme.colorScheme.rn3Error,
)
