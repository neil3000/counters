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
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.rahmouni.neil.counters.core.auth.LocalAuthHelper
import dev.rahmouni.neil.counters.core.designsystem.LocalSharedTransitionScope
import dev.rahmouni.neil.counters.core.feedback.feedbackDialog
import dev.rahmouni.neil.counters.core.user.Rn3User.LoggedOutUser
import dev.rahmouni.neil.counters.feature.aboutme.aboutMeScreen
import dev.rahmouni.neil.counters.feature.aboutme.navigateToAboutMe
import dev.rahmouni.neil.counters.feature.dashboard.DASHBOARD_ROUTE
import dev.rahmouni.neil.counters.feature.dashboard.dashboardScreen
import dev.rahmouni.neil.counters.feature.dashboard.navigateToDashboard
import dev.rahmouni.neil.counters.feature.login.LOGIN_ROUTE
import dev.rahmouni.neil.counters.feature.login.loginScreen
import dev.rahmouni.neil.counters.feature.login.navigateToLogin
import dev.rahmouni.neil.counters.feature.settings.navigateToSettings
import dev.rahmouni.neil.counters.feature.settings.settingsNavigation
import dev.rahmouni.neil.counters.ui.CountersAppState
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DesignSystem")
@Composable
fun CountersNavHost(
    appState: CountersAppState,
    modifier: Modifier = Modifier,
    showLoginScreen: Boolean,
) {
    val auth = LocalAuthHelper.current

    val navController = appState.navController

    LaunchedEffect(Unit) {
        delay(100)
        navController.navigate(if (auth.getUser() is LoggedOutUser || showLoginScreen) LOGIN_ROUTE else DASHBOARD_ROUTE) {
            popUpTo("SPLASHSCREEN_SET_ROUTE") { inclusive = true }
        }
    }

    Scaffold {
        SharedTransitionLayout {
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                NavHost(
                    navController = navController,
                    startDestination = "SPLASHSCREEN_SET_ROUTE",
                    modifier = modifier,
                ) {
                    aboutMeScreen(navController)
                    dashboardScreen(navController, navController::navigateToSettings)
                    loginScreen(navController) {
                        navController.navigateToDashboard {
                            popUpTo(LOGIN_ROUTE) { inclusive = true }
                        }
                    }
                    settingsNavigation(
                        navController,
                        navController::navigateToLogin,
                        navController::navigateToAboutMe,
                    )

                    feedbackDialog(navController)

                    composable("SPLASHSCREEN_SET_ROUTE") {
                        Box(
                            Modifier
                                .background(Color(136, 18, 41))
                                .sharedElement(
                                    rememberSharedContentState(key = "countersLogo_background"),
                                    animatedVisibilityScope = this@composable,
                                ),
                        ) {
                            Spacer(Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}
