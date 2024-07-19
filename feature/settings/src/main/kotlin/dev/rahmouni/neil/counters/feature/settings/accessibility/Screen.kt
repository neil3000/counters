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

package dev.rahmouni.neil.counters.feature.settings.accessibility

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Description
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
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.AccessibilitySettingsUiState
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.AccessibilitySettingsUiState.Loading
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.AccessibilitySettingsUiState.Success
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.AccessibilitySettingsViewModel
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.AccessibilitySettingsData
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.AccessibilitySettingsDataPreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.PreviewParameterData
import dev.rahmouni.neil.counters.feature.settings.logDataAndPrivacySettingsUiEvent

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
        setAltText = viewModel::setAltText,
        onClickAndroidAccessibilityTile = {
            analyticsHelper.logDataAndPrivacySettingsUiEvent("androidAccessibilityTile")
            context.openAndroidAccessibilitySettingsActivity()
        },
    )

    TrackScreenViewEvent(screenName = "AccessibilitySettings")
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
    setAltText: (Boolean) -> Unit = {},
    onClickAndroidAccessibilityTile: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_settings_accessibilitySettingsScreen_topAppBarTitle),
        onBackIconButtonClicked = onBackIconButtonClicked,
        topAppBarActions = listOfNotNull(feedbackTopAppBarAction),
    ) {
        when (uiState) {
            Loading -> {}
            is Success -> AccessibilitySettingsPanel(
                it,
                uiState.accessibilitySettingsData,
                setEmphasizedSwitches,
                setIconTooltips,
                setAltText,
                onClickAndroidAccessibilityTile,
            )
        }
    }
}

@Composable
private fun AccessibilitySettingsPanel(
    paddingValues: Rn3PaddingValues,
    data: AccessibilitySettingsData,
    setEmphasizedSwitches: (Boolean) -> Unit,
    setIconTooltips: (Boolean) -> Unit,
    setAltText: (Boolean) -> Unit,
    onClickAndroidAccessibilityTile: () -> Unit,
) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
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

        // iconTooltipsTile
        Rn3TileSwitch(
            title = stringResource(string.feature_settings_settingsScreen_iconAltTextTile_title),
            icon = Outlined.Description,
            supportingText = stringResource(string.feature_settings_settingsScreen_iconAltTextTile_supportingText),
            checked = data.hasAltTextEnabled,
            onCheckedChange = setAltText,
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
            uiState = Success(PreviewParameterData.accessibilitySettingsData_default),
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
        LocalAccessibilityHelper provides AccessibilityHelper(
            hasEmphasizedSwitchesEnabled = accessibilitySettingsData.hasEmphasizedSwitchesEnabled,
            hasIconTooltipsEnabled = accessibilitySettingsData.hasIconTooltipsEnabled,
            hasAltTextEnabled = accessibilitySettingsData.hasAltTextEnabled,
        ),
    ) {
        Rn3Theme {
            AccessibilitySettingsScreen(uiState = Success(accessibilitySettingsData = accessibilitySettingsData))
        }
    }
}
