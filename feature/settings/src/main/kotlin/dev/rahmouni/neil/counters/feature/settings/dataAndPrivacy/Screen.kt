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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.common.Rn3Uri
import dev.rahmouni.neil.counters.core.common.toRn3Uri
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewUiStates
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.ExpandableSurface
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LazyColumnFullScreen
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileUri
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
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
    navController: NavController,
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val analytics = LocalAnalyticsHelper.current
    val config = LocalConfigHelper.current

    //TODO clear metrics
    DataAndPrivacySettingsScreen(
        modifier,
        uiState,
        onBackIconButtonClicked = navController::popBackStack,
        onFeedbackIconButtonClicked = {
            navController.navigateToFeedback(
                context,
                FeedbackScreenContext(
                    "DataAndPrivacySettingsScreen",
                    "Cql6OgxiWaapWpb38eGNGRZbmFuR8JuQ",
                ),
            )
        },
        onMetricsTileCheckedChange = viewModel::setMetricsEnabled,
        onClearMetricsTileClicked = analytics::clearMetrics,
        onCrashlyticsTileCheckedChange = viewModel::setCrashlyticsEnabled,
        privacyPolicyTileUri = config.getString("privacy_policy_url")
            .toRn3Uri(analytics::logAndroidAccessibilityTileClicked),
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DataAndPrivacySettingsScreen(
    modifier: Modifier = Modifier,
    uiState: DataAndPrivacySettingsUiState,
    onBackIconButtonClicked: () -> Unit = {},
    onFeedbackIconButtonClicked: () -> Unit = {},
    onMetricsTileCheckedChange: (Boolean) -> Unit = {},
    onClearMetricsTileClicked: () -> Unit = {},
    onCrashlyticsTileCheckedChange: (Boolean) -> Unit = {},
    privacyPolicyTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
) {
    Rn3Scaffold(
        modifier,
        stringResource(string.feature_settings_dataAndPrivacySettingsScreen_topAppBar_title),
        onBackIconButtonClicked,
        onFeedbackIconButtonClicked,
    ) {
        when (uiState) {
            Loading -> {}
            is Success -> DataAndPrivacySettingsPanel(
                it,
                data = uiState.dataAndPrivacySettingsData,
                onMetricsTileCheckedChange,
                onClearMetricsTileClicked,
                onCrashlyticsTileCheckedChange,
                privacyPolicyTileUri,
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
    onCrashlyticsTileCheckedChange: (Boolean) -> Unit,
    privacyPolicyTileUri: Rn3Uri,
) {
    val config = LocalConfigHelper.current

    Rn3LazyColumnFullScreen(contentPadding = contentPadding) {
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
            ExpandableSurface(
                content = {
                    Icon(Icons.Outlined.Info, null)
                    Spacer(Modifier.width(16.dp))
                    Text(stringResource(string.feature_settings_dataAndPrivacySettingsScreen_metricsInfoTile_title))
                },
                expandedContent = {
                    Text(
                        config.getString("metrics_info_short"),
                        Modifier.padding(start = 16.dp, bottom = 8.dp),
                    )
                },
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
            ExpandableSurface(
                content = {
                    Icon(Icons.Outlined.Info, null)
                    Spacer(Modifier.width(16.dp))
                    Text(stringResource(string.feature_settings_dataAndPrivacySettingsScreen_crashlyticsInfoTile_title))
                },
                expandedContent = {
                    Text(
                        config.getString("crashlytics_info_short"),
                        Modifier.padding(start = 16.dp, bottom = 8.dp),
                    )
                },
            )
        }

        item { Rn3TileHorizontalDivider() }

        // privacyPolicyHeaderTile
        item { Rn3TileSmallHeader(title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_privacyPolicyHeaderTile_title)) }

        // privacyPolicyTile
        item {
            Rn3TileUri(
                title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_privacyPolicyTile_title),
                icon = Icons.Outlined.Policy,
                uri = privacyPolicyTileUri,
            )
        }

        // privacyPolicySummaryTile
        item {
            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
            ) {
                Column {
                    Rn3TileSmallHeader(title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_privacyPolicySummaryTile_title))
                    Text(
                        text = config.getString("privacy_policy_short"),
                        Modifier.padding(start = 16.dp, bottom = 8.dp, end = 16.dp),
                    )
                }
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
