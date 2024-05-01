package dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val DATA_AND_PRIVACY_SETTINGS_ROUTE = "settings/dataAndPrivacy"

fun NavController.navigateToDataAndPrivacySettings(navOptions: NavOptions? = null) = navigate(
    DATA_AND_PRIVACY_SETTINGS_ROUTE, navOptions
)

fun NavGraphBuilder.dataAndPrivacySettingsScreen(navController: NavController) {
    composable(route = DATA_AND_PRIVACY_SETTINGS_ROUTE) {
        DataAndPrivacySettingsRoute(
            onBackIconButtonClicked = { navController.popBackStack() },
        )
    }
}