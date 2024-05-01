package dev.rahmouni.neil.counters.feature.settings.main

import android.content.Context
import android.provider.Settings
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.common.openLink
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LargeTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.feedback.getFeedbackID
import dev.rahmouni.neil.counters.feature.settings.R.string
import dev.rahmouni.neil.counters.feature.settings.logChangelogTileClicked

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
        onBackIconButtonClicked = onBackIconButtonClicked,
        onClickDataAndPrivacyTile = onClickDataAndPrivacyTile,
        onClickAccessibilityTile = onClickAccessibilityTile,
        isChangelogAvailable = config.getString("changelog_url") != "null",
        onClickChangelogTile = {
            context.openLink(config.getString("changelog_url"))
            analytics.logChangelogTileClicked()
        },
        onClickContributeTile = onClickContributeTile,
        onClickAboutMeTile = onClickAboutMeTile,
        onClickDeveloperSettings = onClickDeveloperSettings,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    showDeveloperSettings: Boolean = false,
    onBackIconButtonClicked: () -> Unit = {},
    onClickDataAndPrivacyTile: () -> Unit = {},
    onClickAccessibilityTile: () -> Unit = {},
    isChangelogAvailable: Boolean = true,
    onClickChangelogTile: () -> Unit = {},
    onClickContributeTile: () -> Unit = {},
    onClickAboutMeTile: () -> Unit = {},
    onClickDeveloperSettings: () -> Unit = {},
) {
    Column(modifier) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                @Suppress("SpellCheckingInspection")
                Rn3LargeTopAppBar(
                    title = stringResource(string.feature_settings_settingsScreen_topAppBar_title),
                    scrollBehavior = scrollBehavior,
                    feedbackPageID = getFeedbackID(
                        localName = "SettingsScreen",
                        localID = "niFsraaAjn2ceEtyaou8hBuxVcKZmL4d",
                    ),
                    onBackIconButtonClicked = onBackIconButtonClicked,
                )
            },
        ) {
            Column(Modifier.padding(it)) {

                // // generalHeaderTile
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

                // // aboutHeaderTile
                Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_aboutHeaderTile_title))

                // changelogTile
                if (isChangelogAvailable) {
                    Rn3TileClick(
                        title = stringResource(string.feature_settings_settingsScreen_changelogTile_title),
                        icon = Outlined.NewReleases,
                        onClick = onClickChangelogTile,
                        external = true,
                    )
                }

                // contributeTile
                Rn3TileClick(
                    title = "Contribute & help", //TODO i18n
                    icon = Outlined.StarBorder,
                    onClick = onClickContributeTile, //TODO
                )

                // aboutMeTile
                Rn3TileClick(
                    title = "About me", //TODO i18n
                    icon = Outlined.Rn3,
                    onClick = onClickAboutMeTile, //TODO
                )

                Rn3TileHorizontalDivider()

                // // advancedSettingsHeaderTile
                Rn3TileSmallHeader(title = stringResource(string.feature_settings_settingsScreen_advancedSettingsHeaderTile_title))

                // developerSettingsTile
                if (showDeveloperSettings) {
                    Rn3TileClick(
                        title = "Developer settings",
                        icon = Outlined.DataObject,
                        supportingText = "Shown since you have Dev Settings on",
                        onClick = onClickDeveloperSettings,
                    )
                }
            }
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