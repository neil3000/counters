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
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val haptic = getHaptic()
    val indication = LocalIndication.current
    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        headlineContent = { Text(text = title) },
        modifier = modifier.toggleable(
            value = checked,
            interactionSource = interactionSource,
            indication = indication,
            role = Role.Switch,
        ) {
            onCheckedChange(it)
            haptic.toggle(it)
        },
        supportingContent = if (supportingText != null) {
            { Text(text = supportingText) }
        } else null,
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        trailingContent = {
            Rn3Switch(
                checked = checked,
                contentDescription = null, // Null as to None -- because text is already present in the Tile itself
                interactionSource = interactionSource,
                onCheckedChange = null,
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
                title = "Title", icon = Outlined.EmojiEvents,
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
                title = "Title", icon = Outlined.EmojiEvents,
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