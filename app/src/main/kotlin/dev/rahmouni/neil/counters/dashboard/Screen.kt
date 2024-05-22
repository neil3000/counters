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

package dev.rahmouni.neil.counters.dashboard

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LazyColumnFullScreen
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.feature.settings.main.navigateToSettings
import rahmouni.neil.counters.R

@Composable
internal fun DashboardRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val context = LocalContext.current

    DashboardScreen(
        modifier = modifier,
        onFeedbackIconButtonClicked = {
            navController.navigateToFeedback(
                context,
                FeedbackScreenContext("DashboardScreen", "8iMorl3UBGcseoIPGzDjkXPzHflkPdFn"),
            )
        },
        onSettingsIconButtonClicked = {
            navController.navigateToSettings()
        },
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DashboardScreen(
    modifier: Modifier = Modifier,
    onFeedbackIconButtonClicked: () -> Unit = {},
    onSettingsIconButtonClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier,
        stringResource(R.string.counters_dashboardScreen_topAppBar_title),
        null,
        onFeedbackIconButtonClicked,
        onSettingsIconButtonClicked,
        {
            FloatingActionButton(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = FloatingActionButtonDefaults.containerColor,
                onClick = { TODO() },
                modifier = Modifier.padding(24.dp),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        },
        topAppBarStyle = TopAppBarStyle.SMALL,
    ) {
        SettingsPanel(
            it,
        )
    }
}

@Composable
private fun SettingsPanel(
    contentPadding: PaddingValues,
) {
    Rn3LazyColumnFullScreen(contentPadding = contentPadding) {

    }
}