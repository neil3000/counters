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

package dev.rahmouni.neil.counters.feature.dashboard.bottomSheet

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument

const val ADDCOUNTER_ROUTE = "addCounter"

fun NavController.navigateToAddCounter(
    navOptions: NavOptions? = null,
    page: String = "name",
) =
    navigate(
        "$ADDCOUNTER_ROUTE?&page=$page",
        navOptions,
    )

fun NavGraphBuilder.addCounterDialog(navController: NavController) {
    dialog(
        route = "$ADDCOUNTER_ROUTE?page={page}",
        dialogProperties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        arguments = listOf(
            navArgument("contextID") {
                defaultValue = null
                nullable = true
            },
            navArgument("page") { defaultValue = "name" },
        ),
    ) {
        AddCounterBottomSheet(
            navController,
            it.arguments?.getString("page") ?: "name",
        )
    }
}
