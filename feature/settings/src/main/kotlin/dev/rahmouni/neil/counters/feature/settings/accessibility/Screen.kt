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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.SettingsAccessibility
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.accessibility.AccessibilityHelper
import dev.rahmouni.neil.counters.core.accessibility.LocalAccessibilityHelper
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.common.openAndroidAccessibilitySettingsActivity
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewUiStates
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.designsystem.icons.Tooltip
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.AccessibilitySettingsUiState
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.AccessibilitySettingsUiState.Loading
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.AccessibilitySettingsUiState.Success
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.AccessibilitySettingsViewModel
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.AccessibilitySettingsData
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.AccessibilitySettingsDataPreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.PreviewParameterData.accessibilitySettingsData_default
import dev.rahmouni.neil.counters.feature.settings.logAndroidAccessibilityTileClicked

@Composable
internal fun AccessibilitySettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: AccessibilitySettingsViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val analyticsHelper = LocalAnalyticsHelper.current

    AccessibilitySettingsScreen(
        modifier,
        uiState,
        onBackIconButtonClicked = navController::popBackStack,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "AccessibilitySettingsScreen",
            "jrKt4Xe58KDipPJsm1iPUijn6BMsNc8g",
        ).toTopAppBarAction(navController::navigateToFeedback),
        setEmphasizedSwitches = viewModel::setEmphasizedSwitches,
        setIconTooltips = viewModel::setIconTooltips,
        onClickAndroidAccessibilityTile = {
            analyticsHelper.logAndroidAccessibilityTileClicked()
            context.openAndroidAccessibilitySettingsActivity()
        },
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun AccessibilitySettingsScreen(
    modifier: Modifier = Modifier,
    uiState: AccessibilitySettingsUiState,
    onBackIconButtonClicked: () -> Unit = {},
    feedbackTopAppBarAction: TopAppBarAction? = null,
    setEmphasizedSwitches: (Boolean) -> Unit = {},
    setIconTooltips: (Boolean) -> Unit = {},
    onClickAndroidAccessibilityTile: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier,
        stringResource(string.feature_settings_accessibilitySettingsScreen_topAppBarTitle),
        onBackIconButtonClicked,
        topAppBarActions = listOfNotNull(feedbackTopAppBarAction),
    ) {
        when (uiState) {
            Loading -> {}
            is Success -> AccessibilitySettingsPanel(
                it,
                uiState.accessibilitySettingsData,
                setEmphasizedSwitches,
                setIconTooltips,
                onClickAndroidAccessibilityTile,
            )
        }
    }
}

@Composable
private fun AccessibilitySettingsPanel(
    contentPadding: PaddingValues,
    data: AccessibilitySettingsData,
    setEmphasizedSwitches: (Boolean) -> Unit,
    setIconTooltips: (Boolean) -> Unit,
    onClickAndroidAccessibilityTile: () -> Unit,
) {
    Column(
        Modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        // emphasizedSwitchesTile
        Rn3TileSwitch(
            title = stringResource(string.feature_settings_settingsScreen_emphasizedSwitchesTile_title),
            icon = Outlined.ToggleOn,
            checked = data.hasEmphasizedSwitchesEnabled,
            onCheckedChange = setEmphasizedSwitches,
        )

        // iconTooltipsTile
        Rn3TileSwitch(
            title = stringResource(string.feature_settings_settingsScreen_iconTooltipsTile_title),
            icon = Outlined.Tooltip,
            supportingText = stringResource(string.feature_settings_settingsScreen_iconTooltipsTile_supportingText),
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
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        AccessibilitySettingsScreen(
            uiState = Success(accessibilitySettingsData_default),
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
                uiState = Success(accessibilitySettingsData = accessibilitySettingsData),
            )
        }
    }
}
