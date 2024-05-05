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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.DataArray
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LargeTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.component.getRn3LargeTopAppBarScrollBehavior
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
    onBackIconButtonClicked: () -> Unit,
) {
    DeveloperSettingsScreen(
        modifier,
        onBackIconButtonClicked,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DeveloperSettingsScreen(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit = {},
) {
    val scrollBehavior = getRn3LargeTopAppBarScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Rn3LargeTopAppBar(
                title = "Developer settings",
                scrollBehavior = scrollBehavior,
                onBackIconButtonClicked = onBackIconButtonClicked,
            )
        },
        contentWindowInsets = WindowInsets.displayCutout,
    ) { paddingValues ->
        DeveloperSettingsPanel(paddingValues)
    }
}

@Composable
private fun DeveloperSettingsPanel(paddingValues: PaddingValues) {
    val config = LocalConfigHelper.current
    val analytics = LocalAnalyticsHelper.current

    LazyColumn(Modifier.padding(paddingValues)) {
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
            Rn3TileCopy(
                title = "RC last fetch status",
                icon = Icons.Outlined.DataArray,
                text = config.getLastFetchStatus(),
            )
        }

        item { Rn3TileHorizontalDivider() }
        item { Rn3TileSmallHeader(title = "RC values") }

        config.forEachEntry { key, value, source ->
            val icon = when (source) {
                "remote" -> Icons.Outlined.CloudDone
                "default" -> Icons.AutoMirrored.Outlined.InsertDriveFile
                else -> Icons.Outlined.QuestionMark
            }

            if (value == "true" || value == "false") {
                item {
                    Rn3TileSwitch(title = key, icon = icon, checked = value == "true") {}
                }
            } else {
                item {
                    Rn3TileCopy(title = key, icon = icon, text = value)
                }
            }
        }

        item { Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.systemBars)) }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        DeveloperSettingsScreen()
    }
}
