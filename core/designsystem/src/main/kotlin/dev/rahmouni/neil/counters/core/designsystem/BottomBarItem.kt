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

package dev.rahmouni.neil.counters.core.designsystem

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class BottomBarItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit,
    var selectedIconColor: Color? = null,
    var unselectedIconColor: Color? = null,
    val selected: Boolean = false,
    val fullSize: Boolean = false,
) {

    @Composable
    fun Icon() {
        if (fullSize) {
            Icon(imageVector = this.icon, contentDescription = this.label, modifier = Modifier.size(50.dp))
        } else {
            Icon(imageVector = this.icon, contentDescription = null)
        }
    }

    @Composable
    fun colors(): NavigationBarItemColors {
        return NavigationBarItemDefaults.colors(
            selectedIconColor = selectedIconColor ?: Color.Unspecified,
            unselectedIconColor = unselectedIconColor ?: Color.Unspecified
        )
    }
}
