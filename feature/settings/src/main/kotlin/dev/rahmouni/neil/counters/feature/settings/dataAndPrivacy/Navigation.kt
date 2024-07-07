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

package dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

internal const val DATA_AND_PRIVACY_ROUTE = "dataAndPrivacy"

internal fun NavController.navigateToDataAndPrivacySettings(navOptions: NavOptions? = null) =
    navigate(
        DATA_AND_PRIVACY_ROUTE,
        navOptions,
    )

internal fun NavGraphBuilder.dataAndPrivacyScreen(navController: NavController) {
    composable(route = DATA_AND_PRIVACY_ROUTE) {
        DataAndPrivacySettingsRoute(
            navController = navController,
        )
    }
}
