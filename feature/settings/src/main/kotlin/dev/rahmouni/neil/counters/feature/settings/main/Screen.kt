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

package dev.rahmouni.neil.counters.feature.settings.main

import android.content.Context
import android.provider.Settings
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.auth.LocalAuthHelper
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.core.auth.user.UserAvatarAndName
import dev.rahmouni.neil.counters.core.common.Rn3Uri
import dev.rahmouni.neil.counters.core.common.openOssLicensesActivity
import dev.rahmouni.neil.counters.core.common.toRn3Uri
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewUiStates
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurface
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClickConfirmationDialog
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDividerDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileUri
import dev.rahmouni.neil.counters.core.designsystem.icons.Contract
import dev.rahmouni.neil.counters.core.designsystem.icons.DevicesOff
import dev.rahmouni.neil.counters.core.designsystem.icons.Discord
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3
import dev.rahmouni.neil.counters.core.designsystem.icons.SyncSavedLocally
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.accessibility.navigateToAccessibilitySettings
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.navigateToDataAndPrivacySettings
import dev.rahmouni.neil.counters.feature.settings.developer.main.navigateToDeveloperSettingsMain
import dev.rahmouni.neil.counters.feature.settings.logChangelogTileClicked
import dev.rahmouni.neil.counters.feature.settings.logDiscordTileClicked
import dev.rahmouni.neil.counters.feature.settings.logOssLicensesTileClicked
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsUiState
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsUiState.Loading
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsUiState.Success
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsViewModel
import dev.rahmouni.neil.counters.feature.settings.main.model.data.PreviewParameterData
import dev.rahmouni.neil.counters.feature.settings.main.model.data.SettingsData
import dev.rahmouni.neil.counters.feature.settings.main.model.data.SettingsDataPreviewParameterProvider
import kotlinx.coroutines.launch

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    navigateToLogin: () -> Unit,
    navigateToAboutMe: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val analytics = LocalAnalyticsHelper.current
    val config = LocalConfigHelper.current
    val auth = LocalAuthHelper.current

    val scope = rememberCoroutineScope()

    viewModel.setDevSettingsEnabled(context.areAndroidDeveloperSettingsOn())

    SettingsScreen(
        modifier = modifier,
        uiState,
        onAccountTileSwitchAccountTileClicked = navigateToLogin,
        onAccountTileLogoutTileClicked = {
            scope.launch { auth.signOut(context) }
            navigateToLogin()
        },
        onAccountTileLoginButtonClicked = navigateToLogin,
        onBackIconButtonClicked = navController::popBackStack,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "SettingsScreen",
            "niFsraaAjn2ceEtyaou8hBuxVcKZmL4d",
        ).toTopAppBarAction(navController::navigateToFeedback),
        onClickDataAndPrivacyTile = navController::navigateToDataAndPrivacySettings,
        onClickAccessibilityTile = navController::navigateToAccessibilitySettings,
        changelogTileUri = config.getString("changelog_url")
            .toRn3Uri(analytics::logChangelogTileClicked),
        discordTileUri = config.getString("discord_invite_url")
            .toRn3Uri(analytics::logDiscordTileClicked),
        onClickContributeTile = { TODO() },
        onClickAboutMeTile = navigateToAboutMe,
        onClickDeveloperSettingsTile = navController::navigateToDeveloperSettingsMain,
        onClickOssLicensesTile = {
            context.openOssLicensesActivity()
            analytics.logOssLicensesTileClicked()
        },
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onAccountTileSwitchAccountTileClicked: () -> Unit = {},
    onAccountTileLogoutTileClicked: () -> Unit = {},
    onAccountTileLoginButtonClicked: () -> Unit = {},
    onBackIconButtonClicked: () -> Unit = {},
    onClickDataAndPrivacyTile: () -> Unit = {},
    onClickAccessibilityTile: () -> Unit = {},
    changelogTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
    discordTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
    onClickContributeTile: () -> Unit = {},
    onClickAboutMeTile: () -> Unit = {},
    onClickDeveloperSettingsTile: () -> Unit = {},
    onClickOssLicensesTile: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier,
        stringResource(string.feature_settings_settingsScreen_topAppBarTitle),
        onBackIconButtonClicked,
        topAppBarActions = listOfNotNull(feedbackTopAppBarAction),
    ) {
        when (uiState) {
            Loading -> {}
            is Success -> SettingsPanel(
                it,
                uiState.settingsData,
                onAccountTileSwitchAccountTileClicked,
                onAccountTileLogoutTileClicked,
                onAccountTileLoginButtonClicked,
                onClickDataAndPrivacyTile,
                onClickAccessibilityTile,
                changelogTileUri,
                discordTileUri,
                onClickContributeTile,
                onClickAboutMeTile,
                onClickDeveloperSettingsTile,
                onClickOssLicensesTile,
            )
        }
    }
}

@Composable
private fun SettingsPanel(
    paddingValues: Rn3PaddingValues,
    data: SettingsData,
    onAccountTileSwitchAccountTileClicked: () -> Unit,
    onAccountTileLogoutTileClicked: () -> Unit,
    onAccountTileLoginButtonClicked: () -> Unit,
    onClickDataAndPrivacyTile: () -> Unit,
    onClickAccessibilityTile: () -> Unit,
    changelogTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
    discordTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
    onClickContributeTile: () -> Unit,
    onClickAboutMeTile: () -> Unit,
    onClickDeveloperSettingsTile: () -> Unit,
    onClickOssLicensesTile: () -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
    ) {
        // accountTile
        when (data.user) {
            is SignedInUser -> Rn3ExpandableSurface(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
                content = { data.user.UserAvatarAndName() },
                expandedContent = {
                    Column {
                        Rn3TileHorizontalDivider(
                            paddingValues = Rn3TileHorizontalDividerDefaults.paddingValues.copy(
                                top = 0.dp,
                                bottom = 0.dp,
                            ),
                        )

                        // switchAccountTile
                        Rn3TileClick(
                            title = "Switch account",
                            icon = Outlined.Group,
                            onClick = onAccountTileSwitchAccountTileClicked,
                        )

                        // logoutTile
                        Rn3TileClickConfirmationDialog(
                            title = stringResource(string.feature_settings_settingsScreen_accountLogoutTile_title),
                            icon = Icons.AutoMirrored.Outlined.Logout,
                            bodyHeader = stringResource(string.feature_settings_settingsScreen_accountLogoutTile_bodyHeader),
                            bodyBulletPoints = mapOf(
                                Outlined.SyncSavedLocally to stringResource(string.feature_settings_settingsScreen_accountLogoutTile_body_currentDataStoredLocallyAndContinueToWorkOnDevice),
                                Outlined.DevicesOff to stringResource(string.feature_settings_settingsScreen_accountLogoutTile_body_countersNotAvailableOnOtherDevices),
                                Outlined.Warning to stringResource(string.feature_settings_settingsScreen_accountLogoutTile_body_mayLoseDataIfSomethingHappensToDevice),
                            ),
                            onClick = {
                                isExpanded = false
                                onAccountTileLogoutTileClicked()
                            },
                        )
                    }
                },
                tonalElevation = 4.dp,
            )

            else -> Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
            ) {
                Row(
                    Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    data.user.UserAvatarAndName()

                    // loginButton
                    OutlinedButton(onClick = onAccountTileLoginButtonClicked) {
                        Text(stringResource(string.feature_settings_settingsScreen_accountLoginTile_loginButton_text))
                    }
                }
            }
        }

        // generalHeaderTile
        Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_generalHeaderTile_title))

        // dataAndPrivacyTile
        Rn3TileClick(
            title = stringResource(string.feature_settings_settingsScreen_dataAndPrivacyTile_title),
            icon = Outlined.Shield,
            onClick = onClickDataAndPrivacyTile,
        )

        // accessibilityTile
        Rn3TileClick(
            title = stringResource(string.feature_settings_settingsScreen_accessibilityTile_title),
            icon = Outlined.AccessibilityNew,
            onClick = onClickAccessibilityTile,
        )

        Rn3TileHorizontalDivider()

        // aboutHeaderTile
        Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_aboutHeaderTile_title))

        // changelogTile
        Rn3TileUri(
            title = stringResource(string.feature_settings_settingsScreen_changelogTile_title),
            icon = Outlined.NewReleases,
            uri = changelogTileUri,
        )

        // discordTile
        Rn3TileUri(
            title = stringResource(string.feature_settings_settingsScreen_discordTile_title),
            icon = Outlined.Discord,
            uri = discordTileUri,
        )

        // contributeTile
        Rn3TileClick(
            title = stringResource(string.feature_settings_settingsScreen_contributeTile_title),
            icon = Outlined.StarBorder,
            onClick = onClickContributeTile,
        )

        // aboutMeTile
        Rn3TileClick(
            title = stringResource(string.feature_settings_settingsScreen_aboutMeTile_title),
            icon = Outlined.Rn3,
            onClick = onClickAboutMeTile,
        )

        Rn3TileHorizontalDivider()

        // otherHeaderTile
        Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_otherHeaderTile_title))

        // developerSettingsTile
        if (data.devSettingsEnabled) {
            Rn3TileClick(
                title = stringResource(string.feature_settings_settingsScreen_developer_title),
                icon = Outlined.DataObject,
                supportingText = stringResource(string.feature_settings_settingsScreen_developer_supportingText),
                onClick = onClickDeveloperSettingsTile,
            )
        }

        // ossLicensesTile
        Rn3TileClick(
            title = stringResource(string.feature_settings_settingsScreen_ossLicensesTile_title),
            icon = Outlined.Contract,
            onClick = onClickOssLicensesTile,
        )
    }
}

@Composable
private fun Context.areAndroidDeveloperSettingsOn(): Boolean {
    if (LocalInspectionMode.current) return true

    return Settings.Secure.getInt(
        this.contentResolver,
        Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
        0,
    ) != 0
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        SettingsScreen(
            uiState = Success(PreviewParameterData.settingsData_default),
        )
    }
}

@Rn3PreviewUiStates
@Composable
private fun UiStates(
    @PreviewParameter(SettingsDataPreviewParameterProvider::class)
    settingsData: SettingsData,
) {
    Rn3Theme {
        SettingsScreen(
            uiState = Success(settingsData = settingsData),
        )
    }
}
