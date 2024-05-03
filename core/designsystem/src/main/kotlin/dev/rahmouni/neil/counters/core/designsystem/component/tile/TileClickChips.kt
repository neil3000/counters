package dev.rahmouni.neil.counters.core.designsystem.component.tile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LazyRowWithPadding
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic

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
    icon: Painter,
    external: Boolean = false,
    onClick: () -> Unit,
    chips: LazyListScope.() -> Unit,
) {
    Rn3TileClickChipsImpl(
        modifier,
        title,
        {
            Icon(painter = icon, null)
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
    ) {
        ListItem(
            headlineContent = { Text(text = title) },
            modifier = modifier.height(40.dp),
            leadingContent = icon,
            trailingContent = if (external) {
                {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                        contentDescription = null,
                    )
                }
            } else null,
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
                title = "Title", icon = Outlined.EmojiEvents, onClick = {},
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