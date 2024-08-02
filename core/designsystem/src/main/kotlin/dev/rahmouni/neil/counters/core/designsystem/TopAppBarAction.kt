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

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic

data class TopAppBarAction(val icon: ImageVector, val title: String, val onClick: () -> Unit) {

    @Composable
    fun IconButton() {
        Rn3IconButton(icon = this.icon, contentDescription = this.title, onClick = this.onClick)
    }

    @Composable
    fun DropdownMenuItem(onClick: (() -> Unit)? = null) {
        val haptic = getHaptic()

        DropdownMenuItem(
            text = { Text(this.title) },
            onClick = {
                haptic.click()
                this.onClick()
                if (onClick != null) onClick()
            },
            leadingIcon = {
                Icon(imageVector = this.icon, contentDescription = null)
            },
        )
    }
}

@Composable
fun List<TopAppBarAction>.DropdownMenu(expanded: Boolean, onDismissRequest: () -> Unit) {
    androidx.compose.material3.DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) scope@{
        this@DropdownMenu.map { it.DropdownMenuItem(onDismissRequest) }
    }
}
