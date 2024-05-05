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

package dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.common.openLink
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewUiStates
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.LazyColumnFullScreen
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.systemBarSpacer
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.feedback.getFeedbackID
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsUiState
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsUiState.Loading
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsUiState.Success
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsViewModel
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.DataAndPrivacySettingsData
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.DataAndPrivacySettingsDataPreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.logAndroidAccessibilityTileClicked

@Composable
internal fun DataAndPrivacySettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: DataAndPrivacySettingsViewModel = hiltViewModel(),
    onBackIconButtonClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val analytics = LocalAnalyticsHelper.current
    val config = LocalConfigHelper.current

    // TODO add crashlyticsInfo & metricsInfo
    DataAndPrivacySettingsScreen(
        modifier,
        uiState = uiState,
        onBackIconButtonClicked,
        onMetricsTileCheckedChange = viewModel::setMetricsEnabled,
        onClearMetricsTileClicked = analytics::clearMetrics,
        onMetricsInfoTileClicked = {},
        onCrashlyticsTileCheckedChange = viewModel::setCrashlyticsEnabled,
        onCrashlyticsInfoTileClicked = {},
        isPrivacyPolicyAvailable = config.getString("privacy_policy_url") != "null",
        onPrivacyPolicyTileClicked = {
            context.openLink(config.getString("privacy_policy_url"))
            analytics.logAndroidAccessibilityTileClicked()
        },
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DataAndPrivacySettingsScreen(
    modifier: Modifier = Modifier,
    uiState: DataAndPrivacySettingsUiState,
    onBackIconButtonClicked: () -> Unit = {},
    onMetricsTileCheckedChange: (Boolean) -> Unit = {},
    onClearMetricsTileClicked: () -> Unit = {},
    onMetricsInfoTileClicked: () -> Unit = {},
    onCrashlyticsTileCheckedChange: (Boolean) -> Unit = {},
    onCrashlyticsInfoTileClicked: () -> Unit = {},
    isPrivacyPolicyAvailable: Boolean = true,
    onPrivacyPolicyTileClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier,
        stringResource(string.feature_settings_dataAndPrivacySettingsScreen_topAppBar_title),
        onBackIconButtonClicked,
        getFeedbackID(
            localName = "DataAndPrivacySettingsScreen",
            localID = "Cql6OgxiWaapWpb38eGNGRZbmFuR8JuQ",
        ),
    ) {
        when (uiState) {
            Loading -> {}
            is Success -> DataAndPrivacySettingsPanel(
                it,
                data = uiState.dataAndPrivacySettingsData,
                onMetricsTileCheckedChange,
                onClearMetricsTileClicked,
                onMetricsInfoTileClicked,
                onCrashlyticsTileCheckedChange,
                onCrashlyticsInfoTileClicked,
                isPrivacyPolicyAvailable,
                onPrivacyPolicyTileClicked,
            )
        }
    }
}

@Composable
private fun DataAndPrivacySettingsPanel(
    contentPadding: PaddingValues,
    data: DataAndPrivacySettingsData,
    onMetricsTileCheckedChange: (Boolean) -> Unit,
    onClearMetricsTileClicked: () -> Unit,
    onMetricsInfoTileClicked: () -> Unit,
    onCrashlyticsTileCheckedChange: (Boolean) -> Unit,
    onCrashlyticsInfoTileClicked: () -> Unit,
    isPrivacyPolicyAvailable: Boolean,
    onPrivacyPolicyTileClicked: () -> Unit,
) {
    LazyColumnFullScreen(contentPadding = contentPadding) {
        // metricsHeaderTile
        item { Rn3TileSmallHeader(title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_metricsHeaderTile_title)) }

        // metricsTile
        item {
            Rn3TileSwitch(
                title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_metricsTile_title),
                icon = Icons.Outlined.Analytics,
                checked = data.hasMetricsEnabled,
                onCheckedChange = onMetricsTileCheckedChange,
            )
        }

        // clearMetricsTile
        item {
            Rn3TileClick(
                title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_clearMetricsTile_title),
                icon = Icons.Outlined.RestartAlt,
                onClick = onClearMetricsTileClicked,
            )
        }

        // metricsInfoTile
        item {
            Rn3TileClick(
                title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_metricsInfoTile_title),
                icon = Icons.Outlined.Info,
                supportingText = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_metricsInfoTile_supportingText),
                onClick = onMetricsInfoTileClicked,
            )
        }

        item { Rn3TileHorizontalDivider() }

        // crashlyticsHeaderTile
        item { Rn3TileSmallHeader(title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_crashlyticsHeaderTile_title)) }

        // crashlyticsTile
        item {
            Rn3TileSwitch(
                title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_crashlyticsTile_title),
                icon = Icons.Outlined.BugReport,
                checked = data.hasCrashlyticsEnabled,
                onCheckedChange = onCrashlyticsTileCheckedChange,
            )
        }

        // crashlyticsInfoTile
        item {
            Rn3TileClick(
                title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_crashlyticsInfoTile_title),
                icon = Icons.Outlined.Info,
                supportingText = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_crashlyticsInfoTile_supportingText),
                onClick = onCrashlyticsInfoTileClicked,
            )
        }

        if (isPrivacyPolicyAvailable) {
            item { Rn3TileHorizontalDivider() }

            // privacyPolicyTile
            item {
                Rn3TileClick(
                    title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_privacyPolicyTile_title),
                    icon = Icons.Outlined.Policy,
                    external = true,
                    onClick = onPrivacyPolicyTileClicked,
                )
            }
        }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        DataAndPrivacySettingsScreen(
            uiState = Success(
                dataAndPrivacySettingsData = DataAndPrivacySettingsData(
                    hasMetricsEnabled = true,
                    hasCrashlyticsEnabled = true,
                ),
            ),
        )
    }
}

@Rn3PreviewUiStates
@Composable
private fun UiStates(
    @PreviewParameter(DataAndPrivacySettingsDataPreviewParameterProvider::class)
    dataAndPrivacySettingsData: DataAndPrivacySettingsData,
) {
    Rn3Theme {
        DataAndPrivacySettingsScreen(
            uiState = Success(dataAndPrivacySettingsData = dataAndPrivacySettingsData),
        )
    }
}
