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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
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
import dev.rahmouni.neil.counters.core.common.openOssLicensesActivity
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewUiStates
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurface
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LargeButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3TextDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClickConfirmationDialog
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.designsystem.component.user.UserAvatarAndName
import dev.rahmouni.neil.counters.core.designsystem.icons.Contract
import dev.rahmouni.neil.counters.core.designsystem.icons.DevicesOff
import dev.rahmouni.neil.counters.core.designsystem.icons.SyncSavedLocally
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.END
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
    navigateToInformation: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val analytics = LocalAnalyticsHelper.current
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
        uiState = uiState,
        onBackIconButtonClicked = navController::popBackStack,
        feedbackTopAppBarAction = FeedbackScreenContext(
            localName = "SettingsScreen",
            localID = "niFsraaAjn2ceEtyaou8hBuxVcKZmL4d",
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
        onClickDeveloperSettingsTile = {
            analytics.logSettingsUiEvent("developerSettingsTile")
            navController.navigateToDeveloperSettingsMain()
        },
        onClickOssLicensesTile = {
            analytics.logSettingsUiEvent("ossLicensesTile")
            context.openOssLicensesActivity()
        },
        onClickInfoTile = {
            analytics.logSettingsUiEvent("informationTile")
            navigateToInformation()
        },
        setTravelMode = viewModel::setTravelMode,
        setFriendsMain = viewModel::setFriendsMain,
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
    onClickDeveloperSettingsTile: () -> Unit = {},
    onClickOssLicensesTile: () -> Unit = {},
    onClickInfoTile: () -> Unit = {},
    setTravelMode: (Boolean) -> Unit = {},
    setFriendsMain: (Boolean) -> Unit = {},
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_settings_settingsScreen_topAppBarTitle),
        onBackIconButtonClicked = onBackIconButtonClicked,
        topAppBarActions = listOfNotNull(feedbackTopAppBarAction),
    ) {
        when (uiState) {
            Loading -> {}
            is Success -> SettingsPanel(
                paddingValues = it,
                data = uiState.settingsData,
                onAccountTileSwitchAccountTileClicked = onAccountTileSwitchAccountTileClicked,
                onAccountTileLogoutTileClicked = onAccountTileLogoutTileClicked,
                onAccountTileLoginButtonClicked = onAccountTileLoginButtonClicked,
                onUpdateAvailableTileClicked = onUpdateAvailableTileClicked,
                onClickDataAndPrivacyTile = onClickDataAndPrivacyTile,
                onClickAccessibilityTile = onClickAccessibilityTile,
                onClickDeveloperSettingsTile = onClickDeveloperSettingsTile,
                onClickOssLicensesTile = onClickOssLicensesTile,
                onClickInfoTile = onClickInfoTile,
                setTravelMode = setTravelMode,
                setFriendsMain = setFriendsMain,
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
    onClickDeveloperSettingsTile: () -> Unit,
    onClickOssLicensesTile: () -> Unit,
    onClickInfoTile: () -> Unit,
    setTravelMode: (Boolean) -> Unit,
    setFriendsMain: (Boolean) -> Unit,
) {
    val haptic = getHaptic()

    Column(
        modifier = Modifier
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
                            paddingValues = Rn3SurfaceDefaults.paddingValues.copy(
                                top = 0.dp,
                                bottom = 0.dp,
                            ),
                        )

                        // switchAccountTile
                        Rn3TileClick(
                            title = stringResource(string.feature_settings_settingsScreen_switchAccountTile_title),
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
            )

            else -> Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Rn3SurfaceDefaults.paddingValues),
                shape = Rn3SurfaceDefaults.shape,
                tonalElevation = Rn3SurfaceDefaults.tonalElevation,
            ) {
                Row(
                    modifier = Modifier.padding(Rn3TextDefaults.paddingValues),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    data.user.UserAvatarAndName()

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minWidth = 16.dp),
                    )

                    // loginButton
                    OutlinedButton(onClick = onAccountTileLoginButtonClicked) {
                        Text(text = stringResource(string.feature_settings_settingsScreen_accountLoginTile_loginButton_text))
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
                    targetValue = if (actionPossible()) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceColorAtElevation(
                            Rn3SurfaceDefaults.tonalElevation,
                        )
                    },
                    label = "Rn3UpdateTileDefaults background color",
                )

                Rn3LargeButton(
                    modifier = Modifier.padding(Rn3SurfaceDefaults.paddingValues.copy(top = 0.dp)),
                    text = {
                        Text(
                            text = when (this@with) {
                                is UpdateAvailable -> stringResource(string.feature_settings_settingsScreen_updateAvailable)
                                is DownloadingUpdate -> stringResource(string.feature_settings_settingsScreen_updateDownloading)
                                is WaitingForRestart -> stringResource(string.feature_settings_settingsScreen_updateRestart)
                                NoUpdateAvailable -> "RahNeil_N3:GsKMAaulylNilvukEZCbJI4jWLXbRRzb"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.contentColorFor(color),
                            modifier = Modifier.padding(Rn3TextDefaults.paddingValues.only(END))
                        )
                    },
                    icon = {
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
                            is UpdateAvailable -> Icon(
                                imageVector = Outlined.FileDownload,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )

                            is WaitingForRestart -> Icon(
                                imageVector = Outlined.RestartAlt,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    },
                    color = color,
                ) {
                    if (actionPossible()) {
                        haptic.click()
                        onUpdateAvailableTileClicked()
                    }
                }
            }
        }

        // generalHeaderTile
        Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_generalHeaderTile_title))

        // editInfoTile
        Rn3TileClick(
            title = stringResource(string.feature_settings_settingsScreen_editInfoTile_title),
            icon = Outlined.Edit,
            onClick = onClickInfoTile,
        )

        // TravelModeTile
        Rn3TileSwitch(
            title = stringResource(string.feature_settings_mainScreen_travelModeTile_title),
            icon = Outlined.Public,
            supportingText = stringResource(string.feature_settings_mainScreen_travelModeTile_supportingText),
            checked = data.hasTravelModeEnabled,
            onCheckedChange = setTravelMode,
        )

        // MainFriendsTile
        Rn3TileSwitch(
            title = stringResource(string.feature_settings_mainScreen_friendsMainTile_title),
            icon = Outlined.Favorite,
            supportingText = stringResource(string.feature_settings_mainScreen_friendsMainTile_supportingText),
            checked = data.hasFriendsMainEnabled,
            onCheckedChange = setFriendsMain,
        )

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
