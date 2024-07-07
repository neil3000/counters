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

package dev.rahmouni.neil.counters.core.designsystem.component.tile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LazyRowWithPadding
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.icons.AnimatedIcon
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3InfiniteRestartAnimatedIcon

@Composable
fun Rn3TileClickChips(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    external: Boolean = false,
    onClick: () -> Unit,
    chips: LazyListScope.() -> Unit,
) {
    Rn3TileClickChipsImpl(
        modifier,
        title,
        {
            Icon(imageVector = icon, null)
        },
        external,
        onClick,
        chips,
    )
}

@Composable
fun Rn3TileClickChips(
    modifier: Modifier = Modifier,
    title: String,
    icon: AnimatedIcon,
    external: Boolean = false,
    onClick: () -> Unit,
    chips: LazyListScope.() -> Unit,
) {
    Rn3TileClickChipsImpl(
        modifier,
        title,
        {
            Rn3InfiniteRestartAnimatedIcon(icon = icon, contentDescription = null)
        },
        external,
        onClick,
        chips,
    )
}

@Composable
private fun Rn3TileClickChipsImpl(
    modifier: Modifier = Modifier,
    title: String,
    icon: @Composable () -> Unit,
    external: Boolean = false,
    onClick: () -> Unit,
    chips: LazyListScope.() -> Unit,
) {
    val haptic = getHaptic()

    Column(
        modifier = Modifier.clickable {
            onClick()
            haptic.click()
        },
        verticalArrangement = Arrangement.spacedBy((-8).dp),
    ) {
        ListItem(
            headlineContent = { Text(text = title) },
            modifier = modifier,
            leadingContent = icon,
            trailingContent = if (external) {
                {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                        contentDescription = null,
                    )
                }
            } else {
                null
            },
        )
        Rn3LazyRowWithPadding(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier.padding(bottom = 8.dp)) {
            chips()
        }
    }
}

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Surface {
            Rn3TileClickChips(
                title = "Title",
                icon = Outlined.EmojiEvents,
                onClick = {},
            ) {
                items(listOf("1 €", "3 €", "5 €", "Custom")) {
                    FilterChip(
                        selected = it == "3 €",
                        onClick = {},
                        label = { Text(it) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                }
            }
        }
    }
}
