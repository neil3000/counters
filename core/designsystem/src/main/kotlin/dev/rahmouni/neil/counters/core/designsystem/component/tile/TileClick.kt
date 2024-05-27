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

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentVariation
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.icons.OpenInNewAnimated
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3TriggerReverseAnimatedIcon

@Composable
fun Rn3TileClick(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    supportingText: String? = null,
    external: Boolean = false,
    enabled: Boolean = true,
    error: Boolean = false,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Rn3TileClick(
        modifier = modifier,
        title = title,
        icon = icon,
        supportingText = supportingText,
        trailingContent = if (external) {
            {
                Rn3TriggerReverseAnimatedIcon(
                    icon = Outlined.OpenInNewAnimated,
                    null,
                    interactionSource = { interactionSource },
                )
            }
        } else null,
        enabled = enabled,
        error = error,
        onClick = onClick,
    )
}

@Composable
fun Rn3TileClick(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    supportingText: String? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    error: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
) {
    val haptic = getHaptic()
    val indication = LocalIndication.current

    val colors = if (error) {
        ListItemDefaults.colors(
            headlineColor = MaterialTheme.colorScheme.error,
            leadingIconColor = MaterialTheme.colorScheme.error,
            supportingColor = MaterialTheme.colorScheme.error,
            trailingIconColor = MaterialTheme.colorScheme.error,
        )
    } else {
        ListItemDefaults.colors()
    }

    ListItem(
        headlineContent = { Text(text = title) },
        colors = colors,
        modifier = modifier
            .hoverable(interactionSource = interactionSource, enabled = enabled)
            .focusable(interactionSource = interactionSource, enabled = enabled)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = enabled,
            ) {
                if (enabled) {
                    haptic.click()
                    onClick()
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
        trailingContent = trailingContent,
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

@Rn3PreviewComponentVariation
@Composable
private fun External() {
    Rn3Theme {
        Surface {
            Rn3TileClick(
                title = "Title",
                icon = Outlined.EmojiEvents,
                external = true,
            ) {}
        }
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun SupportingTextExternal() {
    Rn3Theme {
        Surface {
            Rn3TileClick(
                title = "Title",
                icon = Outlined.EmojiEvents,
                supportingText = "Supporting text",
                external = true,
            ) {}
        }
    }
}
