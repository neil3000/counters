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

package dev.rahmouni.neil.counters

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.rahmouni.neil.counters.core.auth.LocalAuthHelper
import dev.rahmouni.neil.counters.core.feedback.feedbackDialog
import dev.rahmouni.neil.counters.feature.aboutme.aboutMeScreen
import dev.rahmouni.neil.counters.feature.aboutme.navigateToAboutMe
import dev.rahmouni.neil.counters.feature.dashboard.dashboardScreen
import dev.rahmouni.neil.counters.feature.dashboard.navigateToDashboard
import dev.rahmouni.neil.counters.feature.settings.navigateToSettings
import dev.rahmouni.neil.counters.feature.settings.settingsNavigation
import dev.rahmouni.neil.counters.ui.CountersAppState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DesignSystem")
@Composable
fun CountersNavHost(
    appState: CountersAppState,
    modifier: Modifier = Modifier,
    startDestination: String,
) {
    val navController = appState.navController
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val auth = LocalAuthHelper.current

    Scaffold {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier,
        ) {
            composable("test") {
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = {
                            scope.launch {
                                auth.signIn(context, false)
                            }
                            navController.navigateToDashboard()
                            // TODO authHelper.signIn(this@MainActivity, true)
                        },
                        modifier = Modifier.align(Alignment.Center),
                    ) {
                        Text("Login")
                    }
                }
            }
            aboutMeScreen(navController)
            dashboardScreen(
                navController,
                navController::navigateToSettings,
            )
            settingsNavigation(navController, navController::navigateToAboutMe)

            feedbackDialog(navController)
        }
    }
}
