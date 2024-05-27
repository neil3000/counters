package dev.rahmouni.neil.counters.feature.settings.developer.links.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.common.copyText
import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlData
import dev.rahmouni.neil.counters.core.designsystem.AnimatedNumber
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LinkRn3UrlData.Tile(onEdit: () -> Unit) {
    val context = LocalContext.current
    val haptics = getHaptic()

    Rn3TileClick(
        modifier = Modifier.combinedClickable(
            onClick = {
                haptics.click()
                context.copyText(path, "https://counters.rahmouni.dev/${path}")
            },
            onLongClick = {
                haptics.longPress()
                onEdit()
            },
        ),
        title = path,
        icon = Icons.Outlined.Link,
        supportingContent = {
            Column {
                description.let { if (it.isNotEmpty()) Text(it, fontStyle = FontStyle.Italic) }
                Text(
                    redirectUrl.removePrefix("https://"),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        trailingContent = {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp),
            ) {
                AnimatedNumber(currentValue = clicks) { targetValue ->
                    Box(Modifier.sizeIn(36.dp, 36.dp)) {
                        Text(
                            text = targetValue.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        },
    ) {}
}