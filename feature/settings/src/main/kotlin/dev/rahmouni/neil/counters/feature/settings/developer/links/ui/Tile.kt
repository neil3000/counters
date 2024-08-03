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

package dev.rahmouni.neil.counters.feature.settings.developer.links.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.common.copyText
import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlRawData
import dev.rahmouni.neil.counters.core.designsystem.AnimatedNumber
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.feature.settings.R
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LinkRn3UrlRawData.Tile(
    modifier: Modifier = Modifier,
    onFavorite: (favorited: Boolean) -> Unit,
    onLongPress: () -> Unit,
) {
    val context = LocalContext.current
    val haptics = getHaptic()

    Rn3TileClick(
        modifier = modifier.combinedClickable(
            onClick = {
                haptics.click()
                context.copyText(label = path, text = "https://counters.rahmouni.dev/$path")
            },
            onLongClick = {
                haptics.longPress()
                onLongPress()
            },
        ),
        title = path,
        leadingContent = {
            Rn3IconButton(
                icon = if (favorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = pluralStringResource(
                    R.plurals.feature_settings_linkRn3UrlTile_favoriteIconButton_contentDescription,
                    favorite.compareTo(false),
                ),
                modifier = Modifier.size(24.dp),
                contentColor = MaterialTheme.colorScheme.secondary.takeIf { favorite }
                    ?: Color.Unspecified,
            ) {
                onFavorite(!favorite)
            }
        },
        supportingContent = {
            Column {
                description.let {
                    if (!it.isNullOrEmpty()) Text(
                        text = it,
                        fontStyle = FontStyle.Italic,
                    )
                }
                Text(
                    text = redirectUrl?.removePrefix("https://")
                        ?.removePrefix("counters.rahmouni.dev")
                        ?: "",
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
                AnimatedNumber(currentValue = clicks.toDouble()) { targetValue ->
                    Box(modifier = Modifier.sizeIn(36.dp, 36.dp)) {
                        Text(
                            text = targetValue.roundToInt().toString(),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        },
    ) {}
}
