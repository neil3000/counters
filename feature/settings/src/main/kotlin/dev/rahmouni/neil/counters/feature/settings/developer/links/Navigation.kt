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

package dev.rahmouni.neil.counters.feature.settings.developer.links

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val DEVELOPER_SETTINGS_LINKS_ROUTE = "links"

internal fun NavController.navigateToDeveloperSettingsLinks(navOptions: NavOptions? = null) =
    navigate(
    DEVELOPER_SETTINGS_LINKS_ROUTE,
    navOptions,
)

internal fun NavGraphBuilder.developerSettingsLinksScreen(navController: NavController) {
    composable(route = DEVELOPER_SETTINGS_LINKS_ROUTE) {
        DeveloperSettingsLinksRoute(
            navController = navController,
        )
    }
}
