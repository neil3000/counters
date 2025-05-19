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

package dev.rahmouni.neil.counters.feature.dashboard.newCounter

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val DASHBOARD_NEWCOUNTER_ROUTE = "new"

internal fun NavController.navigateToDashboardNewCounter(navOptions: NavOptions? = null) =
    navigate(DASHBOARD_NEWCOUNTER_ROUTE, navOptions)

internal fun NavGraphBuilder.newCounterScreen(navController: NavController) {
    composable(route = DASHBOARD_NEWCOUNTER_ROUTE) {
        NewCounterRoute(navController = navController)
    }
}
