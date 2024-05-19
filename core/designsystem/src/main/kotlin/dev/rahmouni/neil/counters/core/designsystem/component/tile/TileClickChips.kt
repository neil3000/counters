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

package dev.rahmouni.neil.counters.core.designsystem.component.tile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
        Modifier.clickable {
            onClick()
            haptic.click()
        },
        Arrangement.spacedBy((-8).dp),
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
        Rn3LazyRowWithPadding(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
