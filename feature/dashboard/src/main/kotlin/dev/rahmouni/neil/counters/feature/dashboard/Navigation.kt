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

package dev.rahmouni.neil.counters.feature.dashboard

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import dev.rahmouni.neil.counters.core.designsystem.LocalNavAnimatedVisibilityScope

const val DASHBOARD_ROUTE = "dashboard"

fun NavController.navigateToDashboard(builder: NavOptionsBuilder.() -> Unit) =
    navigate(DASHBOARD_ROUTE, builder)

fun NavGraphBuilder.dashboardScreen(
    navController: NavController,
    navigateToSettings: () -> Unit,
) {
    composable(route = DASHBOARD_ROUTE) {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            DashboardRoute(
                navController = navController,
                navigateToSettings = navigateToSettings,
            )
        }
    }
}
