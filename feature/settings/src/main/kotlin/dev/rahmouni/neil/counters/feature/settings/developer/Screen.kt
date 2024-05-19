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

package dev.rahmouni.neil.counters.feature.settings.developer

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.DataArray
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LazyColumnFullScreen
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileCopy
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3
import dev.rahmouni.neil.counters.feature.settings.BuildConfig

@Composable
internal fun DeveloperSettingsRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    DeveloperSettingsScreen(
        modifier,
        onBackIconButtonClicked = navController::popBackStack,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DeveloperSettingsScreen(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier, "Developer settings",
        onBackIconButtonClicked,
        null,
    ) {
        DeveloperSettingsPanel(it)
    }
}

@Composable
private fun DeveloperSettingsPanel(
    contentPadding: PaddingValues,
) {
    val config = LocalConfigHelper.current
    val analytics = LocalAnalyticsHelper.current

    Rn3LazyColumnFullScreen(contentPadding = contentPadding) {
        item {
            Rn3TileClick(
                title = "Build config",
                icon = Icons.Outlined.Rn3,
                supportingText = BuildConfig.FLAVOR + " / " + BuildConfig.BUILD_TYPE,
            ) {}
        }

        item {
            Rn3TileCopy(
                title = "Firebase App Installation ID",
                icon = Icons.Outlined.LocalFireDepartment,
                text = analytics.appInstallationID,
            )
        }

        item {
            Rn3TileClick(
                title = "RC last fetch status",
                icon = Icons.Outlined.DataArray,
                supportingText = config.getLastFetchStatus(),
            ) {}
        }

        item { Rn3TileHorizontalDivider() }
        item { Rn3TileSmallHeader(title = "RC values") }

        config.forEachEntry { key, value, source ->
            val icon = when (source) {
                "remote" -> Icons.Outlined.CloudDone
                "default" -> Icons.AutoMirrored.Outlined.InsertDriveFile
                else -> Icons.Outlined.QuestionMark
            }

            if (value.isBoolean()) {
                item {
                    Rn3TileSwitch(title = key, icon = icon, checked = value == "true") {}
                }
            } else {
                item {
                    Rn3TileCopy(title = key, icon = icon, text = value)
                }
            }
        }
    }
}

private fun String.isBoolean(): Boolean = this == "true" || this == "false"

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        DeveloperSettingsScreen()
    }
}
