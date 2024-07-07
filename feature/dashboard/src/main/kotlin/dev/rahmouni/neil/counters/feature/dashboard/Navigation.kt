/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
