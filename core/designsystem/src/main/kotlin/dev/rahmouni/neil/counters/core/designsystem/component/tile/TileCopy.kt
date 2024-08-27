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
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import dev.rahmouni.neil.counters.core.common.copyText
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic

@Composable
fun Rn3TileCopy(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    text: String,
    truncate: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val haptic = getHaptic()
    val context = LocalContext.current

    ListItem(
        headlineContent = { Text(text = title) },
        modifier = modifier.clickable {
            if (onClick != null) onClick()
            context.copyText(title, text)
            haptic.click()
        },
        supportingContent = {
            Text(
                text = text,
                maxLines = if (truncate) 1 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        trailingContent = {
            Icon(imageVector = Outlined.ContentCopy, contentDescription = null)
        },
    )
}

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Surface {
            Rn3TileCopy(
                title = "Title",
                icon = Outlined.EmojiEvents,
                text = "Copy me :D",
            )
        }
    }
}

@Rn3PreviewComponentDefault
@Composable
private fun TextTooLong() {
    Rn3Theme {
        Surface {
            Rn3TileCopy(
                title = "Title",
                icon = Outlined.EmojiEvents,
                text = "This is an unusually long text that definitely should be truncated if the text to be copied is an ID or something unintelligible",
            )
        }
    }
}

@Rn3PreviewComponentDefault
@Composable
private fun TextTooLongNotTruncated() {
    Rn3Theme {
        Surface {
            Rn3TileCopy(
                title = "Title",
                icon = Outlined.EmojiEvents,
                text = "This is an unusually long text that definitely should be truncated if the text to be copied is an ID or something unintelligible",
                truncate = false,
            )
        }
    }
}
