package dev.rahmouni.neil.counters.core.designsystem.component.tile

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentVariation
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic

@Composable
fun Rn3TileClick(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    supportingText: String? = null,
    external: Boolean = false,
    onClick: () -> Unit,
) {
    val haptic = getHaptic()

    ListItem(
        headlineContent = { Text(text = title) },
        modifier = modifier.clickable {
            onClick()
            haptic.click()
        },
        supportingContent = if (supportingText != null) {
            { Text(text = supportingText) }
        } else null,
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        trailingContent = if (external) {
            { Icon(imageVector = Icons.AutoMirrored.Outlined.OpenInNew, contentDescription = null) }
        } else null,
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
                title = "Title", icon = Outlined.EmojiEvents, external = true,
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