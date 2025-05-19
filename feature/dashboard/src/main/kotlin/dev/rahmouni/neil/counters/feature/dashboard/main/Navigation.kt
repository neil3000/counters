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

package dev.rahmouni.neil.counters.feature.dashboard.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val DASHBOARD_MAIN_ROUTE = "main"

internal fun NavController.navigateToDashboardMain(navOptions: NavOptions? = null) =
    navigate(DASHBOARD_MAIN_ROUTE, navOptions)

internal fun NavGraphBuilder.mainScreen(
    navController: NavController,
    navigateToSettings: () -> Unit,
) {
    composable(route = DASHBOARD_MAIN_ROUTE) {
        DashboardRoute(
            navController = navController,
            navigateToSettings = navigateToSettings,
        )
    }
}
