package dev.rahmouni.neil.counters.feature.settings.accessibility

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ACCESSIBILITY_SETTINGS_ROUTE = "settings/accessibility"

fun NavController.navigateToAccessibilitySettings(navOptions: NavOptions? = null) = navigate(
    ACCESSIBILITY_SETTINGS_ROUTE, navOptions
)

fun NavGraphBuilder.accessibilitySettingsScreen(navController: NavController) {
    composable(route = ACCESSIBILITY_SETTINGS_ROUTE) {
        AccessibilitySettingsRoute(
            onBackIconButtonClicked = { navController.popBackStack() },
        )
    }
}