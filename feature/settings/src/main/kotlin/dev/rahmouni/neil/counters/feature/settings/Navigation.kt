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

package dev.rahmouni.neil.counters.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import dev.rahmouni.neil.counters.feature.settings.accessibility.accessibilityScreen
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.dataAndPrivacyScreen
import dev.rahmouni.neil.counters.feature.settings.developer.developerSettingsNavigation
import dev.rahmouni.neil.counters.feature.settings.main.SETTINGS_MAIN_ROUTE
import dev.rahmouni.neil.counters.feature.settings.main.mainScreen

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) =
    navigate(SETTINGS_ROUTE, navOptions)

fun NavGraphBuilder.settingsNavigation(
    navController: NavController,
    navigateToLogin: () -> Unit,
    navigateToAboutMe: () -> Unit,
) {
    navigation(startDestination = SETTINGS_MAIN_ROUTE, route = SETTINGS_ROUTE) {
        mainScreen(navController, navigateToLogin, navigateToAboutMe)
        accessibilityScreen(navController)
        dataAndPrivacyScreen(navController)
        developerSettingsNavigation(navController)
    }
}
