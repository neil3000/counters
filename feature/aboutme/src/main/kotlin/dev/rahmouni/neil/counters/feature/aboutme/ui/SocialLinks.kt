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
            Rn3TileSmallHeader(title = stringResource(R.string.feature_aboutme_socialLinks_headerTile_title))
            Rn3LazyRowWithPadding(
                Modifier.padding(bottom = 4.dp),
                horizontalPadding = 8.dp,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                items(socialLinks.filter { it.uri is Available }) {
                    Rn3IconButton(
                        icon = it.getIcon(),
                        contentDescription = it.tooltip,
                    ) {
                        // TODO analytics (it.id)

                        // Not preloaded on purpose so opened in apps that handle these links instead of being opened in browser
                        context.openUri(it.uri, false)
                    }
                }
            }
        }
    }
}
