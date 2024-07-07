/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.feature.aboutme.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.common.Rn3Uri.Available
import dev.rahmouni.neil.counters.core.common.openUri
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LazyRowWithPadding
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeaderDefaults
import dev.rahmouni.neil.counters.feature.aboutme.R
import dev.rahmouni.neil.counters.feature.aboutme.model.data.SocialLink

@Composable
fun SocialLinks(socialLinks: List<SocialLink>) {
    val context = LocalContext.current

    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Rn3TileSmallHeader(
                title = stringResource(R.string.feature_aboutme_socialLinks_headerTile_title),
                paddingValues = Rn3TileSmallHeaderDefaults.paddingValues.copy(bottom = 0.dp),
            )
            Rn3LazyRowWithPadding(
                Modifier.padding(bottom = 8.dp),
                horizontalPadding = 8.dp,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                items(socialLinks.filter { it.uri is Available }) {
                    Rn3IconButton(
                        icon = it.getIcon(),
                        contentDescription = it.tooltip,
                    ) {
                        // Not preloaded on purpose so its opened in apps that handle these links instead of being opened in browser
                        context.openUri(it.uri, false)
                    }
                }
            }
        }
    }
}
