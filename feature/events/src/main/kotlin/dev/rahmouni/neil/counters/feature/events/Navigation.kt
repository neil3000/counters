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

package dev.rahmouni.neil.counters.feature.events

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.rahmouni.neil.counters.core.designsystem.LocalNavAnimatedVisibilityScope

const val EVENTS_ROUTE = "events"

fun NavController.navigateToEvents(navOptions: NavOptions? = null) =
    navigate(route = EVENTS_ROUTE, navOptions = navOptions)

fun NavGraphBuilder.eventsScreen(
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToConnect: () -> Unit,
    navigateToFriends: () -> Unit,
    navigateToPublication: () -> Unit,
    navigateToPublic: () -> Unit,
) {
    composable(route = EVENTS_ROUTE) {
        CompositionLocalProvider(value = LocalNavAnimatedVisibilityScope provides this) {
            EventsRoute(
                navController = navController,
                navigateToSettings = navigateToSettings,
                navigateToConnect = navigateToConnect,
                navigateToFriends = navigateToFriends,
                navigateToPublication = navigateToPublication,
                navigateToPublic = navigateToPublic,
            )
        }
    }
}
