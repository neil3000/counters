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

import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.common.Rn3Uri
import dev.rahmouni.neil.counters.core.common.Rn3Uri.AndroidPreview
import dev.rahmouni.neil.counters.core.common.Rn3Uri.Available
import dev.rahmouni.neil.counters.core.common.Rn3Uri.InMaintenance
import dev.rahmouni.neil.counters.core.common.Rn3Uri.SoonAvailable
import dev.rahmouni.neil.counters.core.common.Rn3Uri.Unavailable
import dev.rahmouni.neil.counters.core.common.openUri
import dev.rahmouni.neil.counters.core.common.prepareToOpenUri
import dev.rahmouni.neil.counters.core.designsystem.R
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentVariation
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme

@Composable
fun Rn3TileUri(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    uri: Rn3Uri,
    supportingText: String? = null,
    forceSupportingText: Boolean = false,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        context.prepareToOpenUri(uri)
    }

    Rn3TileClick(
        modifier = modifier,
        title = title,
        icon = icon,
        supportingText = if (forceSupportingText) {
            supportingText
        } else {
            when (uri) {
                is AndroidPreview -> supportingText
                is InMaintenance -> stringResource(R.string.core_designsystem_tileUri_inMaintenance_supportingText)
                is Unavailable -> stringResource(R.string.core_designsystem_tileUri_unavailable_supportingText)
                is SoonAvailable -> stringResource(R.string.core_designsystem_tileUri_soonAvailable_supportingText)
                is Available -> supportingText
            }
        },
        external = true,
        enabled = uri is AndroidPreview || uri is Available,
    ) {
        context.openUri(uri)
    }
}

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Surface {
            Rn3TileUri(
                title = "Title",
                icon = Outlined.EmojiEvents,
                uri = AndroidPreview,
            )
        }
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun InMaintenance() {
    Rn3Theme {
        Surface {
            Rn3TileUri(
                title = "Title",
                icon = Outlined.EmojiEvents,
                uri = InMaintenance,
            )
        }
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun Unavailable() {
    Rn3Theme {
        Surface {
            Rn3TileUri(
                title = "Title",
                icon = Outlined.EmojiEvents,
                uri = Unavailable,
            )
        }
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun SoonAvailable() {
    Rn3Theme {
        Surface {
            Rn3TileUri(
                title = "Title",
                icon = Outlined.EmojiEvents,
                uri = SoonAvailable,
            )
        }
    }
}
