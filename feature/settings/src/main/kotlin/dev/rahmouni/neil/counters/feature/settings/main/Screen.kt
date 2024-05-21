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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.auth.LocalAuthHelper
import dev.rahmouni.neil.counters.core.auth.user.Avatar
import dev.rahmouni.neil.counters.core.auth.user.Rn3User
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.LoggedOutUser
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.core.common.Rn3Uri
import dev.rahmouni.neil.counters.core.common.openOssLicensesActivity
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
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDividerDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileUri
import dev.rahmouni.neil.counters.core.designsystem.icons.Contract
import dev.rahmouni.neil.counters.core.designsystem.icons.Discord
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.accessibility.navigateToAccessibilitySettings
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.navigateToDataAndPrivacySettings
import dev.rahmouni.neil.counters.feature.settings.developer.navigateToDeveloperSettings
import dev.rahmouni.neil.counters.feature.settings.logChangelogTileClicked
import dev.rahmouni.neil.counters.feature.settings.logDiscordTileClicked
import dev.rahmouni.neil.counters.feature.settings.logOssLicensesTileClicked
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsUiState
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
        onAccountTileLogoutClicked = {
            scope.launch {
                auth.signOut(context)
            }
        },
        onAccountTileLoginClicked = {
            scope.launch {
                auth.signInWithCredentialManager(context, false)
            }
        },
        onBackIconButtonClicked = navController::popBackStack,
        onFeedbackIconButtonClicked = {
            navController.navigateToFeedback(
                context,
                FeedbackScreenContext("SettingsScreen", "niFsraaAjn2ceEtyaou8hBuxVcKZmL4d"),
            )
        },
        onClickDataAndPrivacyTile = navController::navigateToDataAndPrivacySettings,
        onClickAccessibilityTile = navController::navigateToAccessibilitySettings,
        changelogTileUri = config.getString("changelog_url")
            .toRn3Uri(analytics::logChangelogTileClicked),
        discordTileUri = config.getString("discord_invite_url")
            .toRn3Uri(analytics::logDiscordTileClicked),
        onClickContributeTile = { TODO() },
        onClickAboutMeTile = navigateToAboutMe,
        onClickDeveloperSettingsTile = navController::navigateToDeveloperSettings,
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
    onAccountTileLogoutClicked: () -> Unit = {},
    onAccountTileLoginClicked: () -> Unit = {},
    onBackIconButtonClicked: () -> Unit = {},
    onFeedbackIconButtonClicked: () -> Unit = {},
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
        stringResource(string.feature_settings_settingsScreen_topAppBar_title),
        onBackIconButtonClicked,
        onFeedbackIconButtonClicked,
    ) {
        SettingsPanel(
            it,
            uiState.settingsData,
            onAccountTileLogoutClicked,
            onAccountTileLoginClicked,
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

@Composable
private fun SettingsPanel(
    contentPadding: PaddingValues,
    data: SettingsData,
    onAccountTileLogoutClicked: () -> Unit,
    onAccountTileLoginClicked: () -> Unit,
    onClickDataAndPrivacyTile: () -> Unit,
    onClickAccessibilityTile: () -> Unit,
    changelogTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
    discordTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
    onClickContributeTile: () -> Unit,
    onClickAboutMeTile: () -> Unit,
    onClickDeveloperSettingsTile: () -> Unit,
    onClickOssLicensesTile: () -> Unit,
) {
    Rn3LazyColumnFullScreen(contentPadding = contentPadding) {
        // generalHeaderTile
        item { Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_generalHeaderTile_title)) }

        // accountTile
        item {
            when (data.user) {
                is SignedInUser -> ExpandableSurface(
                    content = {
                        UserAvatarAndName(data.user)
                    },
                    expandedContent = {
                        Column {
                            Rn3TileHorizontalDivider(
                                paddingValues = Rn3TileHorizontalDividerDefaults.paddingValues.copy(
                                    top = 0.dp,
                                ),
                            )
                            Rn3TileSwitch(
                                title = "Device sync",
                                icon = Outlined.Sync,
                                checked = true,
                                onCheckedChange = {},
                            )
                            Rn3TileClick(
                                title = "Logout",
                                icon = Icons.AutoMirrored.Outlined.Logout,
                                onClick = onAccountTileLogoutClicked,
                                error = true,
                            )
                        }
                    },
                    tonalElevation = 4.dp,
                )

                is LoggedOutUser -> Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                ) {
                    Row(
                        Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        UserAvatarAndName(user = data.user)
                        OutlinedButton(onClick = onAccountTileLoginClicked) {
                            Text("Login")
                        }
                    }
                }
            }
        }

        // dataAndPrivacyTile
        item {
            Rn3TileClick(
                title = stringResource(string.feature_settings_settingsScreen_dataAndPrivacyTile_title),
                icon = Outlined.Shield,
                onClick = onClickDataAndPrivacyTile,
            )
        }

        // accessibilityTile
        item {
            Rn3TileClick(
                title = stringResource(string.feature_settings_settingsScreen_accessibilityTile_title),
                icon = Outlined.AccessibilityNew,
                onClick = onClickAccessibilityTile,
            )
        }

        item {
            Rn3TileHorizontalDivider()
        }

        // aboutHeaderTile
        item {
            Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_aboutHeaderTile_title))
        }

        // changelogTile
        item {
            Rn3TileUri(
                title = stringResource(string.feature_settings_settingsScreen_changelogTile_title),
                icon = Outlined.NewReleases,
                uri = changelogTileUri,
            )
        }

        // discordTile
        item {
            Rn3TileUri(
                title = stringResource(string.feature_settings_settingsScreen_discordTile_title),
                icon = Outlined.Discord,
                uri = discordTileUri,
            )
        }

        // contributeTile
        item {
            Rn3TileClick(
                title = stringResource(string.feature_settings_settingsScreen_contributeTile_title),
                icon = Outlined.StarBorder,
                onClick = onClickContributeTile,
            )
        }

        // aboutMeTile
        item {
            Rn3TileClick(
                title = stringResource(string.feature_settings_settingsScreen_aboutMeTile_title),
                icon = Outlined.Rn3,
                onClick = onClickAboutMeTile,
            )
        }

        item { Rn3TileHorizontalDivider() }

        // otherHeaderTile
        item { Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_otherHeaderTile_title)) }

        // developerSettingsTile
        if (data.devSettingsEnabled) item {
            Rn3TileClick(
                title = "Developer settings",
                icon = Outlined.DataObject,
                supportingText = "Shown since you have Dev Settings on",
                onClick = onClickDeveloperSettingsTile,
            )
        }

        // ossLicensesTile
        item {
            Rn3TileClick(
                title = stringResource(string.feature_settings_settingsScreen_ossLicensesTile_title),
                icon = Outlined.Contract,
                onClick = onClickOssLicensesTile,
            )
        }
    }
}

@Composable
private fun UserAvatarAndName(user: Rn3User, modifier: Modifier = Modifier) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        user.Avatar()
        Text(user.getDisplayName(), style = MaterialTheme.typography.bodyLarge)
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
            uiState = SettingsUiState(PreviewParameterData.settingsData_default),
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
            uiState = SettingsUiState(settingsData = settingsData),
        )
    }
}