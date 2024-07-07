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

package dev.rahmouni.neil.counters.core.designsystem.component.tile

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentVariation
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Switch
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic

@Composable
fun Rn3TileSwitch(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    supportingText: String? = null,
    enabled: Boolean = true,
    thumbContent: @Composable (() -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val haptic = getHaptic()
    val indication = LocalIndication.current
    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        headlineContent = { Text(text = title) },
        modifier = modifier
            .toggleable(
                value = checked,
                interactionSource = interactionSource,
                indication = indication,
                role = Role.Switch,
                enabled = enabled,
            ) {
                if (enabled) {
                    onCheckedChange(it)
                    haptic.toggle(it)
                }
            }
            .alpha(if (enabled) 1f else .5f),
        supportingContent = if (supportingText != null) {
            { Text(text = supportingText) }
        } else {
            null
        },
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        trailingContent = {
            Rn3Switch(
                checked = checked,
                contentDescription = null,
                interactionSource = interactionSource,
                onCheckedChange = null,
                enabled = enabled,
                thumbContent = thumbContent,
            )
        },
    )
}

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Surface {
            Rn3TileClick(
                title = "Title",
                icon = Outlined.EmojiEvents,
            ) {}
        }
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun On() {
    Rn3Theme {
        Surface {
            Rn3TileClick(
                title = "Title",
                icon = Outlined.EmojiEvents,
            ) {}
        }
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun SupportingText() {
    Rn3Theme {
        Surface {
            Rn3TileClick(
                title = "Title",
                icon = Outlined.EmojiEvents,
                supportingText = "Supporting text",
            ) {}
        }
    }
}
