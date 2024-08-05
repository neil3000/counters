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

package dev.rahmouni.neil.counters.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding

@Composable
fun Rn3LargeButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.surface,
    leadingIcon: ImageVector? = null,
    shape: Shape = Rn3SurfaceDefaults.shape,
    onClick: () -> Unit,
) {
    Rn3LargeButton(
        modifier = modifier,
        text = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.contentColorFor(color),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = if (leadingIcon != null) 16.dp else 0.dp, end = 16.dp)
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
            )
        },
        color = color,
        leadingIcon = leadingIcon,
        shape = shape,
    ) {
        onClick()
    }
}

@Composable
fun Rn3LargeButton(
    modifier: Modifier = Modifier,
    text: @Composable (() -> Unit),
    icon: @Composable (() -> Unit),
    color: Color = MaterialTheme.colorScheme.surface,
    leadingIcon: ImageVector? = null,
    shape: Shape = Rn3SurfaceDefaults.shape,
    onClick: () -> Unit,
) {
    val haptic = getHaptic()

    Surface(
        modifier = modifier,
        color = color,
        shape = shape,
        tonalElevation = -LocalAbsoluteTonalElevation.current,
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    haptic.click()
                    onClick()
                }
                .padding(Rn3TextDefaults.largePaddingValues),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(modifier = Modifier.weight(1f),) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                    )
                }

                text()
            }

            icon()
        }
    }
}
