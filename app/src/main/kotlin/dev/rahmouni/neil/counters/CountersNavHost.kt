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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import dev.rahmouni.neil.counters.core.feedback.feedbackDialog
import dev.rahmouni.neil.counters.feature.aboutme.aboutMeScreen
import dev.rahmouni.neil.counters.feature.aboutme.navigateToAboutMe
import dev.rahmouni.neil.counters.feature.settings.accessibility.accessibilitySettingsScreen
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.dataAndPrivacySettingsScreen
import dev.rahmouni.neil.counters.feature.settings.developer.developerSettingsScreen
import dev.rahmouni.neil.counters.feature.settings.main.SETTINGS_ROUTE
import dev.rahmouni.neil.counters.feature.settings.main.settingsScreen
import dev.rahmouni.neil.counters.ui.CountersAppState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DesignSystem")
@Composable
fun CountersNavHost(
    appState: CountersAppState,
    modifier: Modifier = Modifier,
    startDestination: String = SETTINGS_ROUTE,
) {
    val navController = appState.navController

    Scaffold {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier,
        ) {
            settingsScreen(navController, navController::navigateToAboutMe)
            accessibilitySettingsScreen(navController)
            developerSettingsScreen(navController)
            dataAndPrivacySettingsScreen(navController)
            aboutMeScreen(navController)

            feedbackDialog(navController)
        }
    }
}
