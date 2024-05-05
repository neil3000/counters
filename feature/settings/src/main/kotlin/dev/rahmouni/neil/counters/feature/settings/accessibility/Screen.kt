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

package dev.rahmouni.neil.counters.feature.settings.accessibility

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.SettingsAccessibility
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.rahmouni.neil.counters.core.accessibility.AccessibilityHelper
import dev.rahmouni.neil.counters.core.accessibility.LocalAccessibilityHelper
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.common.openAndroidAccessibilitySettingsActivity
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewUiStates
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LargeTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.designsystem.icons.Tooltip
import dev.rahmouni.neil.counters.core.feedback.getFeedbackID
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.AccessibilitySettingsUiState
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.AccessibilitySettingsViewModel
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.AccessibilitySettingsData
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.AccessibilitySettingsDataPreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.logAndroidAccessibilityTileClicked

@Composable
internal fun AccessibilitySettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: AccessibilitySettingsViewModel = hiltViewModel(),
    onBackIconButtonClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val analyticsHelper = LocalAnalyticsHelper.current

    AccessibilitySettingsScreen(
        modifier,
        uiState = uiState,
        onBackIconButtonClicked,
        setEmphasizedSwitches = viewModel::setEmphasizedSwitches,
        setIconTooltips = viewModel::setIconTooltips,
        onClickAndroidAccessibilityTile = {
            analyticsHelper.logAndroidAccessibilityTileClicked()
            context.openAndroidAccessibilitySettingsActivity()
        },
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccessibilitySettingsScreen(
    modifier: Modifier = Modifier,
    uiState: AccessibilitySettingsUiState,
    onBackIconButtonClicked: () -> Unit = {},
    setEmphasizedSwitches: (Boolean) -> Unit = {},
    setIconTooltips: (Boolean) -> Unit = {},
    onClickAndroidAccessibilityTile: () -> Unit = {},
) {
    Column(modifier) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                @Suppress("SpellCheckingInspection")
                Rn3LargeTopAppBar(
                    title = stringResource(string.feature_settings_accessibilitySettingsScreen_topAppBar_title),
                    scrollBehavior = scrollBehavior,
                    feedbackPageID = getFeedbackID(
                        localName = "AccessibilitySettingsScreen",
                        localID = "KLlwmK9HscvcmW00fYfONf6scddqsugd",
                    ),
                    onBackIconButtonClicked = onBackIconButtonClicked,
                )
            },
        ) { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                when (uiState) {
                    AccessibilitySettingsUiState.Loading -> {}
                    is AccessibilitySettingsUiState.Success -> AccessibilitySettingsPanel(
                        data = uiState.accessibilitySettingsData,
                        setEmphasizedSwitches,
                        setIconTooltips,
                        onClickAndroidAccessibilityTile,
                    )
                }
            }
        }
    }
}

@Composable
private fun AccessibilitySettingsPanel(
    data: AccessibilitySettingsData,
    setEmphasizedSwitches: (Boolean) -> Unit,
    setIconTooltips: (Boolean) -> Unit,
    onClickAndroidAccessibilityTile: () -> Unit,
) {
    // emphasizedSwitchesTile
    Rn3TileSwitch(
        title = stringResource(string.feature_settings_settingsScreen_emphasizedSwitchesTile_title),
        icon = Outlined.ToggleOn,
        checked = data.hasEmphasizedSwitchesEnabled,
        onCheckedChange = setEmphasizedSwitches,
    )

    // iconTooltipsTile //TODO i18n
    Rn3TileSwitch(
        title = "Icon tooltips",
        icon = Outlined.Tooltip,
        supportingText = "Enable informative popups when you long press icon buttons",
        checked = data.hasIconTooltipsEnabled,
        onCheckedChange = setIconTooltips,
    )

    Rn3TileHorizontalDivider()

    // androidAccessibilityTile
    Rn3TileClick(
        title = stringResource(string.feature_settings_settingsScreen_androidAccessibilityTile_title),
        icon = Outlined.SettingsAccessibility,
        onClick = onClickAndroidAccessibilityTile,
        external = true,
    )
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        AccessibilitySettingsScreen(
            uiState = AccessibilitySettingsUiState.Success(
                accessibilitySettingsData = AccessibilitySettingsData(
                    hasEmphasizedSwitchesEnabled = false,
                    hasIconTooltipsEnabled = true,
                ),
            ),
        )
    }
}

@Rn3PreviewUiStates
@Composable
private fun UiStates(
    @PreviewParameter(AccessibilitySettingsDataPreviewParameterProvider::class)
    accessibilitySettingsData: AccessibilitySettingsData,
) {
    CompositionLocalProvider(
        LocalAccessibilityHelper provides AccessibilityHelper(hasEmphasizedSwitchesEnabled = accessibilitySettingsData.hasEmphasizedSwitchesEnabled),
    ) {
        Rn3Theme {
            AccessibilitySettingsScreen(
                uiState = AccessibilitySettingsUiState.Success(accessibilitySettingsData = accessibilitySettingsData),
            )
        }
    }
}
