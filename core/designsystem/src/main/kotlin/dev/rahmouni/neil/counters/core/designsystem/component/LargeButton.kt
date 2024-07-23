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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun Rn3LargeButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.surface,
    leadingIcon: ImageVector? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    onClick: () -> Unit,
) {
    val haptics = getHaptic()

    Surface(
        color = color,
        shape = shape,
        modifier = modifier.requiredWidthIn(min = 280.dp),
        tonalElevation = -LocalAbsoluteTonalElevation.current,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    haptics.click()
                    onClick()
                }
                .padding(16.dp)
                .fillMaxWidth(1f),
        ) {
            if (leadingIcon != null) {
                Icon(leadingIcon, null, Modifier.padding(end = 16.dp))
            }
            Text(
                text = text,
                color = MaterialTheme.colorScheme.contentColorFor(color),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(4.dp),
            )
            Spacer(Modifier.weight(1f))
            Icon(
                icon,
                null,
                Modifier
                    .padding(start = 16.dp)
                    .width(24.dp),
            )
        }
    }
}
