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

package dev.rahmouni.neil.counters.feature.login.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.roundedCorners.Rn3RoundedCorners

@Composable
internal fun AnonymousTile(shape: Rn3RoundedCorners, onClick: () -> Unit) {
    Surface(tonalElevation = 8.dp, shape = shape.toComposeShape()) {
        Rn3TileClick(
            title = "Without an account",
            icon = Icons.Outlined.NoAccounts,
            supportingContent = { Text("You can sign in later anytime") },
            trailingContent = {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null)
            },
            onClick = onClick,
        )
    }
}
