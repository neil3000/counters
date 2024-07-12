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
import dev.rahmouni.neil.counters.feature.localfeed.LOCALFEED_ROUTE
import dev.rahmouni.neil.counters.feature.localfeed.localFeedScreen
import dev.rahmouni.neil.counters.feature.localfeed.navigateToLocalFeed
import dev.rahmouni.neil.counters.feature.login.LOGIN_ROUTE
import dev.rahmouni.neil.counters.feature.login.loginScreen
import dev.rahmouni.neil.counters.feature.login.navigateToLogin
import dev.rahmouni.neil.counters.feature.settings.navigateToSettings
import dev.rahmouni.neil.counters.feature.publication.navigateToPublication
import dev.rahmouni.neil.counters.feature.publication.publicationScreen
import dev.rahmouni.neil.counters.feature.settings.settingsNavigation
import dev.rahmouni.neil.counters.ui.AppState
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DesignSystem")
@Composable
fun NavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
    showLoginScreen: Boolean,
) {
    val auth = LocalAuthHelper.current

    val navController = appState.navController

    LaunchedEffect(Unit) {
        delay(100)
        navController.navigate(if (auth.getUser() is LoggedOutUser || showLoginScreen) LOGIN_ROUTE else LOCALFEED_ROUTE) {
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
                    localFeedScreen(navController, navController::navigateToSettings, {}, navController::navigateToPublication, {}, {})
                    loginScreen(navController) {
                        navController.navigateToLocalFeed {
                            popUpTo(LOGIN_ROUTE) { inclusive = true }
                        }
                    }
                    settingsNavigation(
                        navController,
                        navController::navigateToLogin,
                    )
                    publicationScreen(navController, navController::navigateToSettings)

                    feedbackDialog(navController)

                    composable("SPLASHSCREEN_SET_ROUTE") {
                        Box(
                            Modifier
                                .background(Color(color = 0xFFE8175D))
                                .sharedElement(
                                    rememberSharedContentState(key = "Logo_background"),
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
