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

package dev.rahmouni.neil.counters.feature.settings.main

import android.content.Context
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.auth.LocalAuthHelper
import dev.rahmouni.neil.counters.core.common.Rn3Uri
import dev.rahmouni.neil.counters.core.common.openOssLicensesActivity
import dev.rahmouni.neil.counters.core.common.toRn3Uri
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewUiStates
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurface
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClickConfirmationDialog
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDividerDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileUri
import dev.rahmouni.neil.counters.core.designsystem.component.user.UserAvatarAndName
import dev.rahmouni.neil.counters.core.designsystem.icons.Contract
import dev.rahmouni.neil.counters.core.designsystem.icons.DevicesOff
import dev.rahmouni.neil.counters.core.designsystem.icons.Discord
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3
import dev.rahmouni.neil.counters.core.designsystem.icons.SyncSavedLocally
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.HORIZONTAL
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.rn3ExpandVerticallyTransition
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.core.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.accessibility.navigateToAccessibilitySettings
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.navigateToDataAndPrivacySettings
import dev.rahmouni.neil.counters.feature.settings.developer.main.navigateToDeveloperSettingsMain
import dev.rahmouni.neil.counters.feature.settings.logSettingsUiEvent
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsUiState
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsUiState.Loading
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsUiState.Success
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsViewModel
import dev.rahmouni.neil.counters.feature.settings.main.model.data.InAppUpdateState.DownloadingUpdate
import dev.rahmouni.neil.counters.feature.settings.main.model.data.InAppUpdateState.NoUpdateAvailable
import dev.rahmouni.neil.counters.feature.settings.main.model.data.InAppUpdateState.UpdateAvailable
import dev.rahmouni.neil.counters.feature.settings.main.model.data.InAppUpdateState.WaitingForRestart
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
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {}

    viewModel.setDevSettingsEnabled(context.areAndroidDeveloperSettingsOn())

    LaunchedEffect(Unit) {
        viewModel.checkForInAppUpdate(AppUpdateManagerFactory.create(context))
    }

    @Suppress("SpellCheckingInspection")
    SettingsScreen(
        modifier = modifier,
        uiState,
        onBackIconButtonClicked = navController::popBackStack,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "SettingsScreen",
            "niFsraaAjn2ceEtyaou8hBuxVcKZmL4d",
        ).toTopAppBarAction(navController::navigateToFeedback),
        onAccountTileSwitchAccountTileClicked = {
            analytics.logSettingsUiEvent("accountTileSwitchAccountTile")
            navigateToLogin()
        },
        onAccountTileLogoutTileClicked = {
            analytics.logSettingsUiEvent("accountTileLogoutTile")

            viewModel.logout()
            scope.launch { auth.signOut(context) }

            navigateToLogin()
        },
        onAccountTileLoginButtonClicked = {
            analytics.logSettingsUiEvent("accountTileLoginButton")
            navigateToLogin()
        },
        onUpdateAvailableTileClicked = {
            viewModel.performInAppUpdateAction(context, launcher)
        },
        onClickDataAndPrivacyTile = {
            analytics.logSettingsUiEvent("dataAndPrivacyTile")
            navController.navigateToDataAndPrivacySettings()
        },
        onClickAccessibilityTile = {
            analytics.logSettingsUiEvent("accessibilityTile")
            navController.navigateToAccessibilitySettings()
        },
        changelogTileUri = config.getString("changelog_url").toRn3Uri {
            analytics.logSettingsUiEvent("changelogTile")
        },
        discordTileUri = config.getString("discord_invite_url").toRn3Uri {
            analytics.logSettingsUiEvent("discordTile")
        },
        onClickContributeTile = {
            analytics.logSettingsUiEvent("contributeTile")
            TODO()
        },
        onClickAboutMeTile = {
            analytics.logSettingsUiEvent("accessibilityTile")
            navigateToAboutMe()
        },
        onClickDeveloperSettingsTile = {
            analytics.logSettingsUiEvent("developerSettingsTile")
            navController.navigateToDeveloperSettingsMain()
        },
        onClickOssLicensesTile = {
            analytics.logSettingsUiEvent("ossLicensesTile")
            context.openOssLicensesActivity()
        },
    )

    TrackScreenViewEvent(screenName = "Settings")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onBackIconButtonClicked: () -> Unit = {},
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onAccountTileSwitchAccountTileClicked: () -> Unit = {},
    onAccountTileLogoutTileClicked: () -> Unit = {},
    onAccountTileLoginButtonClicked: () -> Unit = {},
    onUpdateAvailableTileClicked: () -> Unit = {},
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
                onUpdateAvailableTileClicked,
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
    onUpdateAvailableTileClicked: () -> Unit,
    onClickDataAndPrivacyTile: () -> Unit,
    onClickAccessibilityTile: () -> Unit,
    changelogTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
    discordTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
    onClickContributeTile: () -> Unit,
    onClickAboutMeTile: () -> Unit,
    onClickDeveloperSettingsTile: () -> Unit,
    onClickOssLicensesTile: () -> Unit,
) {
    val haptics = getHaptic()

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
    ) {
        // accountTile
        when (data.user) {
            is SignedInUser -> Rn3ExpandableSurface(
                content = { data.user.UserAvatarAndName(showEmail = it) },
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

        // updateAvailableTile
        with(data.inAppUpdateData) {
            AnimatedVisibility(
                visible = shouldShowTile(),
                enter = rn3ExpandVerticallyTransition(),
            ) {
                val color by animateColorAsState(
                    targetValue = if (actionPossible())
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceColorAtElevation(
                        Rn3ExpandableSurfaceDefaults.tonalElevation,
                    ),
                    label = "Rn3ExpandableSurfaceDefaults background color",
                )

                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            Rn3ExpandableSurfaceDefaults.paddingValues
                                .only(HORIZONTAL)
                                .add(vertical = 4.dp),
                        ),
                    color = color,
                    shape = Rn3ExpandableSurfaceDefaults.shape,
                ) {
                    Row(
                        Modifier
                            .clickable(enabled = actionPossible()) {
                                haptics.click()
                                onUpdateAvailableTileClicked()
                            }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            when (this@with) {
                                is UpdateAvailable -> "Update available"
                                is DownloadingUpdate -> "Downloading..."
                                is WaitingForRestart -> "Restart to apply"
                                NoUpdateAvailable -> "RahNeil_N3:GsKMAaulylNilvukEZCbJI4jWLXbRRzb"
                            },
                            Modifier.padding(top = 2.dp),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        when (this@with) {
                            is DownloadingUpdate -> {
                                val progressFloat by animateFloatAsState(
                                    targetValue = progress,
                                    label = "inAppUpdate CircularProgressIndicator progress",
                                )
                                CircularProgressIndicator(
                                    progress = { progressFloat },
                                    modifier = Modifier.size(24.dp),
                                    trackColor = MaterialTheme.colorScheme.surface,
                                )
                            }

                            NoUpdateAvailable -> {}
                            is UpdateAvailable -> Icon(Outlined.FileDownload, null)
                            is WaitingForRestart -> Icon(Outlined.RestartAlt, null)
                        }
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
