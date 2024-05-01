package dev.rahmouni.neil.counters

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import dev.rahmouni.neil.counters.feature.aboutme.aboutMeScreen
import dev.rahmouni.neil.counters.feature.aboutme.navigateToAboutMe
import dev.rahmouni.neil.counters.feature.settings.accessibility.accessibilitySettingsScreen
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.dataAndPrivacySettingsScreen
import dev.rahmouni.neil.counters.feature.settings.developer.developerSettingsScreen
import dev.rahmouni.neil.counters.feature.settings.main.SETTINGS_ROUTE
import dev.rahmouni.neil.counters.feature.settings.main.settingsScreen
import dev.rahmouni.neil.counters.ui.CountersAppState

@Composable
fun CountersNavHost(
    appState: CountersAppState,
    modifier: Modifier = Modifier,
    startDestination: String = SETTINGS_ROUTE,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        settingsScreen(navController, navController::navigateToAboutMe)
        accessibilitySettingsScreen(navController)
        developerSettingsScreen(navController)
        dataAndPrivacySettingsScreen(navController)
        aboutMeScreen(navController)
    }
}
