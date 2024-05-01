package dev.rahmouni.neil.counters.feature.settings.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.rahmouni.neil.counters.feature.settings.accessibility.navigateToAccessibilitySettings
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.navigateToDataAndPrivacySettings
import dev.rahmouni.neil.counters.feature.settings.developer.navigateToDeveloperSettings

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) =
    navigate(SETTINGS_ROUTE, navOptions)

fun NavGraphBuilder.settingsScreen(navController: NavController, navigateToAboutMe: () -> Unit) {
    composable(route = SETTINGS_ROUTE) {
        SettingsRoute(
            onBackIconButtonClicked = navController::popBackStack,
            onClickDataAndPrivacyTile = navController::navigateToDataAndPrivacySettings,
            onClickAccessibilityTile = navController::navigateToAccessibilitySettings,
            onClickContributeTile = {}, //TODO
            onClickAboutMeTile = navigateToAboutMe,
            onClickDeveloperSettings = navController::navigateToDeveloperSettings,
        )
    }
}