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

package dev.rahmouni.neil.counters.feature.connect

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.rahmouni.neil.counters.core.designsystem.LocalNavAnimatedVisibilityScope

const val CONNECT_ROUTE = "connect"

fun NavController.navigateToConnect(navOptions: NavOptions? = null) =
    navigate(CONNECT_ROUTE, navOptions)

fun NavGraphBuilder.connectScreen(
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToFriends: () -> Unit,
    navigateToPublication: () -> Unit,
    navigateToPublic: () -> Unit,
    navigateToEvents: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable(route = CONNECT_ROUTE) {
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            ConnectRoute(
                navController = navController,
                navigateToSettings = navigateToSettings,
                navigateToFriends = navigateToFriends,
                navigateToPublication = navigateToPublication,
                navigateToPublic = navigateToPublic,
                navigateToEvents = navigateToEvents,
                navigateToLogin = navigateToLogin,
            )
        }
    }
}
