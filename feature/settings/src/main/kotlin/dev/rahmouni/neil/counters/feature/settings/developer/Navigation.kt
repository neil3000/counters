package dev.rahmouni.neil.counters.feature.settings.developer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val DEVELOPER_SETTINGS_ROUTE = "settings/developer"

fun NavController.navigateToDeveloperSettings(navOptions: NavOptions? = null) = navigate(
    DEVELOPER_SETTINGS_ROUTE, navOptions,
)

fun NavGraphBuilder.developerSettingsScreen(navController: NavController) {
    composable(route = DEVELOPER_SETTINGS_ROUTE) {
        DeveloperSettingsRoute(
            onBackIconButtonClicked = { navController.popBackStack() },
        )
    }
}