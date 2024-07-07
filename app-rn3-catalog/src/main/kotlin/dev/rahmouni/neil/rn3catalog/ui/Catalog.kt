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

package dev.rahmouni.neil.rn3catalog.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.AudioFile
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Blind
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.common.Rn3Uri
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurface
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LazyRowWithPadding
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Switch
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SwitchAccessibilityEmphasizedThumbContent
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClickChips
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileCopy
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileUri
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3InfiniteRestartAnimatedIcon
import dev.rahmouni.neil.counters.core.designsystem.icons.customAnimatedIconsList
import dev.rahmouni.neil.counters.core.designsystem.icons.customIconsList
import dev.rahmouni.neil.rn3catalog.itemWithBoolean
import dev.rahmouni.neil.rn3catalog.itemWithToast

private const val CONTENT_DESCRIPTION = "Content description"

@SuppressLint("VisibleForTests")
@Composable
fun Rn3Catalog() {
    Rn3Theme {
        Rn3Scaffold(
            topAppBarTitle = "Rn3 Catalog",
            onBackIconButtonClicked = {},
        ) { paddingValues ->
            LazyColumn(contentPadding = paddingValues.toComposePaddingValues()) {
                item {
                    Rn3ExpandableSurface(
                        content = {
                            Icon(Outlined.Info, null)
                            Spacer(Modifier.width(16.dp))
                            Text("Learn more")
                        },
                        expandedContent = {
                            Text(
                                text = "Lorem ipsum blag blag blah MEZKNGvmzls,gMRKSWFNbwmdfl,bwDLFnhbl!kwd:f;bwdfb",
                                Modifier.padding(start = 16.dp, bottom = 8.dp),
                            )
                        },
                    )
                }
                item { Rn3TileSmallHeader(title = "Switches") }
                item {
                    Rn3LazyRowWithPadding(horizontalPadding = 4.dp) {
                        itemWithBoolean { value, toggleValue ->
                            Rn3Switch(
                                checked = value,
                                onCheckedChange = toggleValue,
                                contentDescription = CONTENT_DESCRIPTION,
                            )
                        }
                        itemWithBoolean { value, toggleValue ->
                            Rn3Switch(
                                checked = value,
                                onCheckedChange = toggleValue,
                                contentDescription = CONTENT_DESCRIPTION,
                                thumbContent = if (value) {
                                    { Rn3SwitchAccessibilityEmphasizedThumbContent() }
                                } else {
                                    null
                                },
                            )
                        }
                        itemWithBoolean { value, toggleValue ->
                            Rn3Switch(
                                checked = value,
                                onCheckedChange = toggleValue,
                                contentDescription = CONTENT_DESCRIPTION,
                                thumbContent = {
                                    Icon(
                                        Outlined.Bluetooth,
                                        CONTENT_DESCRIPTION,
                                        Modifier.size(SwitchDefaults.IconSize),
                                    )
                                },
                            )
                        }
                    }
                }
                item { Rn3TileSmallHeader(title = "Icon buttons") }
                item {
                    Rn3LazyRowWithPadding(
                        horizontalPadding = 4.dp,
                        horizontalArrangement = spacedBy(8.dp),
                    ) {
                        itemWithToast {
                            Rn3IconButton(
                                icon = Outlined.Shield,
                                contentDescription = CONTENT_DESCRIPTION,
                                onClick = it,
                            )
                        }
                        itemWithToast {
                            Rn3IconButton(
                                icon = Outlined.AccessibilityNew,
                                contentDescription = CONTENT_DESCRIPTION,
                                tooltip = "Custom tooltip",
                                onClick = it,
                            )
                        }
                        itemWithToast {
                            Rn3IconButton(
                                icon = Outlined.ToggleOn,
                                contentDescription = CONTENT_DESCRIPTION,
                                tooltip = null,
                                onClick = it,
                            )
                        }
                    }
                }
                item { Rn3TileSmallHeader(title = "Tile click") }
                itemWithToast {
                    Rn3TileClick(
                        title = "Default",
                        icon = Outlined.AcUnit,
                        onClick = it,
                    )
                }
                itemWithToast {
                    Rn3TileClick(
                        title = "External",
                        icon = Outlined.Blind,
                        external = true,
                        onClick = it,
                    )
                }
                itemWithToast {
                    Rn3TileClick(
                        title = "Default",
                        icon = Outlined.AudioFile,
                        supportingText = "With supporting text",
                        onClick = it,
                    )
                }
                itemWithToast {
                    Rn3TileClick(
                        title = "External",
                        icon = Outlined.Cake,
                        supportingText = "With supporting text",
                        external = true,
                        onClick = it,
                    )
                }
                item { Rn3TileHorizontalDivider() }
                item { Rn3TileSmallHeader(title = "Tile switch") }
                itemWithBoolean { value, toggleValue ->
                    Rn3TileSwitch(
                        title = "Default",
                        icon = Outlined.Android,
                        checked = value,
                        onCheckedChange = toggleValue,
                    )
                }
                itemWithBoolean { value, toggleValue ->
                    Rn3TileSwitch(
                        title = "Default",
                        icon = Outlined.Backup,
                        supportingText = "With supporting text",
                        checked = value,
                        onCheckedChange = toggleValue,
                    )
                }
                item { Rn3TileHorizontalDivider() }
                item { Rn3TileSmallHeader(title = "Tile copy") }
                item {
                    Rn3TileCopy(
                        title = "Title",
                        icon = Outlined.EmojiEvents,
                        text = "This is an unusually long text that should be truncated if it is an ID / unintelligible",
                    )
                }
                item {
                    Rn3TileCopy(
                        title = "Not truncated",
                        icon = Outlined.EmojiEvents,
                        text = "This is an unusually long text that should be truncated if it is an ID / unintelligible",
                        truncate = false,
                    )
                }
                item { Rn3TileHorizontalDivider() }
                item { Rn3TileSmallHeader(title = "Icons") }
                item {
                    val icons = customIconsList

                    Rn3LazyRowWithPadding(
                        Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = spacedBy(24.dp),
                    ) {
                        items(icons) {
                            Icon(it, CONTENT_DESCRIPTION)
                        }
                    }
                }
                item { Rn3TileHorizontalDivider() }
                item { Rn3TileSmallHeader(title = "Animated icons (infinite restart)") }
                item {
                    val icons = customAnimatedIconsList

                    Rn3LazyRowWithPadding(
                        Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = spacedBy(24.dp),
                    ) {
                        items(icons) {
                            Rn3InfiniteRestartAnimatedIcon(
                                icon = it,
                                contentDescription = CONTENT_DESCRIPTION,
                            )
                        }
                    }
                }
                item { Rn3TileHorizontalDivider() }
                item { Rn3TileSmallHeader(title = "Tile click chips") }
                itemWithToast { toast ->
                    Rn3TileClickChips(
                        title = "Title",
                        icon = Outlined.EmojiEvents,
                        onClick = toast,
                    ) {
                        items(listOf("1 €", "3 €", "5 €", "Custom")) {
                            FilterChip(
                                selected = it == "3 €",
                                onClick = toast,
                                label = { Text(it) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onTertiary,
                                ),
                            )
                        }
                    }
                }
                item { Rn3TileSmallHeader(title = "Tile Uri") }
                items(
                    listOf(
                        Rn3Uri.AndroidPreview,
                        Rn3Uri.InMaintenance,
                        Rn3Uri.Unavailable,
                        Rn3Uri.SoonAvailable,
                    ),
                ) {
                    Rn3TileUri(title = it.toString(), icon = Outlined.Link, uri = it)
                }
                item {
                    Rn3TileUri(
                        title = "AndroidPreview",
                        icon = Outlined.Link,
                        uri = Rn3Uri.AndroidPreview,
                        supportingText = "With supporting text",
                    )
                }
                item {
                    Rn3TileUri(
                        title = "InMaintenance",
                        icon = Outlined.Link,
                        uri = Rn3Uri.InMaintenance,
                        supportingText = "With forced supporting text",
                        forceSupportingText = true,
                    )
                }
            }
        }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Catalog()
}
