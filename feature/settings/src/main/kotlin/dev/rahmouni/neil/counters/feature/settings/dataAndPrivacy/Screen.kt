/*
 * Copyright (C) 2024 Rahmouni Neïl & Aurélien Coppée
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.ToggleOff
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
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
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurface
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3TextDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClickConfirmationDialog
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileUri
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.HORIZONTAL
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.feature.settings.R
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsUiState
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsUiState.Loading
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsUiState.Success
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.DataAndPrivacySettingsViewModel
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.DataAndPrivacySettingsData
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.DataAndPrivacySettingsDataPreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.PreviewParameterData
import dev.rahmouni.neil.counters.feature.settings.logDataAndPrivacySettingsUiEvent

@Composable
internal fun DataAndPrivacySettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: DataAndPrivacySettingsViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val analytics = LocalAnalyticsHelper.current
    val config = LocalConfigHelper.current

    DataAndPrivacySettingsScreen(
        modifier = modifier,
        uiState = uiState,
        onBackIconButtonClicked = navController::popBackStack,
        feedbackTopAppBarAction = FeedbackScreenContext(
            localName = "DataAndPrivacySettingsScreen",
            localID = "Cql6OgxiWaapWpb38eGNGRZbmFuR8JuQ",
        ).toTopAppBarAction(navigateToFeedback = navController::navigateToFeedback),
        onMetricsTileCheckedChange = viewModel::setMetricsEnabled,
        onClearMetricsTileClicked = analytics::clearMetrics,
        onCrashlyticsTileCheckedChange = viewModel::setCrashlyticsEnabled,
        privacyPolicyTileUri = config.getString(key = "privacy_policy_url").toRn3Uri {
            analytics.logDataAndPrivacySettingsUiEvent(uiId = "privacyPolicyTile")
        },
    )

    TrackScreenViewEvent(screenName = "DataAndPrivacySettings")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DataAndPrivacySettingsScreen(
    modifier: Modifier = Modifier,
    uiState: DataAndPrivacySettingsUiState,
    onBackIconButtonClicked: () -> Unit = {},
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onMetricsTileCheckedChange: (Boolean) -> Unit = {},
    onClearMetricsTileClicked: () -> Unit = {},
    onCrashlyticsTileCheckedChange: (Boolean) -> Unit = {},
    privacyPolicyTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_topAppBarTitle),
        onBackIconButtonClicked = onBackIconButtonClicked,
        topAppBarActions = listOfNotNull(feedbackTopAppBarAction),
    ) {
        when (uiState) {
            Loading -> {}
            is Success -> DataAndPrivacySettingsPanel(
                paddingValues = it,
                data = uiState.dataAndPrivacySettingsData,
                onMetricsTileCheckedChange = onMetricsTileCheckedChange,
                onClearMetricsTileClicked = onClearMetricsTileClicked,
                onCrashlyticsTileCheckedChange = onCrashlyticsTileCheckedChange,
                privacyPolicyTileUri = privacyPolicyTileUri,
            )
        }
    }
}

@Composable
private fun DataAndPrivacySettingsPanel(
    paddingValues: Rn3PaddingValues,
    data: DataAndPrivacySettingsData,
    onMetricsTileCheckedChange: (Boolean) -> Unit,
    onClearMetricsTileClicked: () -> Unit,
    onCrashlyticsTileCheckedChange: (Boolean) -> Unit,
    privacyPolicyTileUri: Rn3Uri,
) {
    val config = LocalConfigHelper.current

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
    ) {
        // metricsHeaderTile
        Rn3TileSmallHeader(title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_metricsHeaderTile_title))

        // metricsTile
        Rn3TileSwitch(
            title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_metricsTile_title),
            icon = Outlined.Analytics,
            checked = data.hasMetricsEnabled,
            onCheckedChange = onMetricsTileCheckedChange,
        )

        // clearMetricsTile
        Rn3TileClickConfirmationDialog(
            title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_clearMetricsTile_title),
            icon = Outlined.RestartAlt,
            bodyHeader = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_clearMetricsTile_bodyHeader),
            bodyBulletPoints = mapOf(
                Outlined.RestartAlt to stringResource(string.feature_settings_settingsScreen_accountLogoutTile_body_onDeviceMetricsDataCleared),
                (if (data.hasMetricsEnabled) Outlined.ToggleOn else Outlined.ToggleOff) to pluralStringResource(
                    R.plurals.feature_settings_settingsScreen_accountLogoutTile_body_metricsRemainStateButCanChangeInSettings,
                    data.hasMetricsEnabled.compareTo(false),
                ),
                Outlined.Warning to stringResource(string.feature_settings_settingsScreen_accountLogoutTile_body_actionCannotBeUndone),
            ),
            onClick = onClearMetricsTileClicked,
        )

        // metricsInfoTile
        Rn3ExpandableSurface(
            content = {
                Icon(imageVector = Outlined.Info, contentDescription = null)
                Text(
                    text = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_metricsInfoTile_title),
                    modifier = Modifier.padding(
                        Rn3TextDefaults.paddingValues.only(HORIZONTAL),
                    ),
                )
            },
            expandedContent = {
                Text(
                    text = config.getString(key = "metrics_info_short"),
                    modifier = Modifier.padding(Rn3TextDefaults.paddingValues.copy(top = 0.dp)),
                )
            },
        )

        Rn3TileHorizontalDivider()

        // crashlyticsHeaderTile
        Rn3TileSmallHeader(title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_crashlyticsHeaderTile_title))

        // crashlyticsTile
        Rn3TileSwitch(
            title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_crashlyticsTile_title),
            icon = Outlined.BugReport,
            checked = data.hasCrashlyticsEnabled,
            onCheckedChange = onCrashlyticsTileCheckedChange,
        )

        // crashlyticsInfoTile
        Rn3ExpandableSurface(
            content = {
                Icon(imageVector = Outlined.Info, contentDescription = null)
                Text(
                    text = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_crashlyticsInfoTile_title),
                    modifier = Modifier.padding(
                        Rn3TextDefaults.paddingValues.only(HORIZONTAL),
                    ),
                )
            },
            expandedContent = {
                Text(
                    text = config.getString(key = "crashlytics_info_short"),
                    modifier = Modifier.padding(Rn3TextDefaults.paddingValues.copy(top = 0.dp)),
                )
            },
        )

        Rn3TileHorizontalDivider()

        // privacyPolicyHeaderTile
        Rn3TileSmallHeader(title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_privacyPolicyHeaderTile_title))

        // privacyPolicyTile
        Rn3TileUri(
            title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_privacyPolicyTile_title),
            icon = Outlined.Policy,
            uri = privacyPolicyTileUri,
        )

        // privacyPolicySummaryTile
        Surface(
            modifier = Modifier
                .padding(Rn3SurfaceDefaults.paddingValues)
                .fillMaxWidth(),
            tonalElevation = Rn3SurfaceDefaults.tonalElevation,
            shape = Rn3SurfaceDefaults.shape,
        ) {
            Column {
                Rn3TileSmallHeader(
                    title = stringResource(string.feature_settings_dataAndPrivacySettingsScreen_privacyPolicySummaryTile_title),
                    paddingValues = Rn3TextDefaults.paddingValues.copy(bottom = 8.dp),
                )
                Text(
                    text = config.getString(key = "privacy_policy_short"),
                    modifier = Modifier.padding(Rn3TextDefaults.paddingValues.copy(top = 0.dp)),
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
            uiState = Success(PreviewParameterData.dataAndPrivacySettingsData_default),
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
            uiState = Success(dataAndPrivacySettingsData),
        )
    }
}
