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

package dev.rahmouni.neil.counters.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import dev.rahmouni.neil.counters.CountersNavHost
import dev.rahmouni.neil.counters.core.auth.LocalAuthHelper
import dev.rahmouni.neil.counters.core.user.Rn3User.LoggedOutUser
import dev.rahmouni.neil.counters.feature.dashboard.DASHBOARD_ROUTE
import dev.rahmouni.neil.counters.feature.login.LOGIN_ROUTE

@Composable
fun CountersApp(
    appState: CountersAppState,
    modifier: Modifier = Modifier,
) {
    val auth = LocalAuthHelper.current
    val startDestination: String by rememberSaveable { mutableStateOf(if (auth.getUser() is LoggedOutUser) LOGIN_ROUTE else DASHBOARD_ROUTE) }

    CountersNavHost(
        appState = appState,
        modifier = modifier,
        startDestination = startDestination,
    )
}
