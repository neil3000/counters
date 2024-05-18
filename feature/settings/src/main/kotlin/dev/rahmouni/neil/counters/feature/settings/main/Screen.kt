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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.common.Rn3Uri
import dev.rahmouni.neil.counters.core.common.openOssLicensesActivity
import dev.rahmouni.neil.counters.core.common.toRn3Uri
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LazyColumnFullScreen
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileUri
import dev.rahmouni.neil.counters.core.designsystem.icons.Contract
import dev.rahmouni.neil.counters.core.designsystem.icons.Discord
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3
import dev.rahmouni.neil.counters.core.ui.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.ui.Rn3Scaffold
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.logChangelogTileClicked
import dev.rahmouni.neil.counters.feature.settings.logDiscordTileClicked
import dev.rahmouni.neil.counters.feature.settings.logOssLicensesTileClicked


@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit,
    onClickDataAndPrivacyTile: () -> Unit,
    onClickAccessibilityTile: () -> Unit,
    onClickContributeTile: () -> Unit,
    onClickAboutMeTile: () -> Unit,
    onClickDeveloperSettings: () -> Unit,
) {
    val context = LocalContext.current
    val analytics = LocalAnalyticsHelper.current
    val config = LocalConfigHelper.current

    SettingsScreen(
        modifier = modifier,
        showDeveloperSettings = context.areAndroidDeveloperSettingsOn(),
        onBackIconButtonClicked,
        onClickDataAndPrivacyTile,
        onClickAccessibilityTile,
        changelogTileUri = config.getString("changelog_url")
            .toRn3Uri(analytics::logChangelogTileClicked),
        discordTileUri = config.getString("discord_invite_url")
            .toRn3Uri(analytics::logDiscordTileClicked),
        onClickContributeTile,
        onClickAboutMeTile,
        onClickDeveloperSettings,
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
    showDeveloperSettings: Boolean = false,
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
        stringResource(string.feature_settings_settingsScreen_topAppBar_title),
        onBackIconButtonClicked,
        FeedbackScreenContext("SettingsScreen", "niFsraaAjn2ceEtyaou8hBuxVcKZmL4d"),
    ) {
        SettingsPanel(
            it,
            showDeveloperSettings = showDeveloperSettings,
            onClickDataAndPrivacyTile = onClickDataAndPrivacyTile,
            onClickAccessibilityTile = onClickAccessibilityTile,
            changelogTileUri = changelogTileUri,
            discordTileUri = discordTileUri,
            onClickContributeTile = onClickContributeTile,
            onClickAboutMeTile = onClickAboutMeTile,
            onClickDeveloperSettingsTile = onClickDeveloperSettingsTile,
            onClickOssLicensesTile = onClickOssLicensesTile,
        )
    }
}

@Composable
private fun SettingsPanel(
    contentPadding: PaddingValues,
    showDeveloperSettings: Boolean,
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
        if (showDeveloperSettings) item {
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
        SettingsScreen()
    }
}
