/*
 * Copyright (C) 2024 Rahmouni Neïl & Aurélien Coppée
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

package dev.rahmouni.neil.counters.feature.settings.developer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import dev.rahmouni.neil.counters.feature.settings.developer.links.developerSettingsLinksScreen
import dev.rahmouni.neil.counters.feature.settings.developer.main.DEVELOPER_SETTINGS_MAIN_ROUTE
import dev.rahmouni.neil.counters.feature.settings.developer.main.developerSettingsMainScreen

const val DEVELOPER_SETTINGS_ROUTE = "developer"

fun NavGraphBuilder.developerSettingsNavigation(navController: NavController) {
    navigation(startDestination = DEVELOPER_SETTINGS_MAIN_ROUTE, route = DEVELOPER_SETTINGS_ROUTE) {
        developerSettingsMainScreen(navController)
        developerSettingsLinksScreen(navController)
    }
}
