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

package dev.rahmouni.neil.counters.feature.dashboard

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LazyColumnFullScreen
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.SMALL
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.feature.dashboard.model.DashboardUiState
import dev.rahmouni.neil.counters.feature.dashboard.model.DashboardViewModel
import dev.rahmouni.neil.counters.feature.dashboard.model.data.DashboardData
import dev.rahmouni.neil.counters.feature.dashboard.model.data.PreviewParameterData
import dev.rahmouni.neil.counters.feature.dashboard.ui.DashboardCard

@Composable
internal fun DashboardRoute(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
    navController: NavController,
    navigateToSettings: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        modifier,
        uiState,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "DashboardScreen",
            "PkS4cSDUBdi2IvRegPIEe46xgk8Bf7h8",
        ).toTopAppBarAction(navController::navigateToFeedback),
        onSettingsTopAppBarActionClicked = navigateToSettings,
        onNewCounterFabClick = {
            viewModel.createUserCounter("Title (Tmp)")
        },
        onIncrementUserCounter = viewModel::incrementUserCounter,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DashboardScreen(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onSettingsTopAppBarActionClicked: () -> Unit = {},
    onNewCounterFabClick: () -> Unit = {},
    onIncrementUserCounter: (String) -> Unit = {},
) {
    val haptics = getHaptic()

    Rn3Scaffold(
        modifier,
        stringResource(R.string.feature_dashboard_dashboardScreen_topAppBarTitle),
        null,
        listOfNotNull(
            TopAppBarAction(
                Icons.Outlined.Settings,
                stringResource(R.string.feature_dashboard_dashboardScreen_topAppBarActions_settings),
                onSettingsTopAppBarActionClicked,
            ),
            feedbackTopAppBarAction,
        ),
        topAppBarStyle = SMALL,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(R.string.feature_dashboard_fab_newCounter)) },
                icon = { Icon(Icons.Outlined.Add, null) },
                onClick = {
                    haptics.click()
                    onNewCounterFabClick()
                },
                Modifier.navigationBarsPadding(),
            )
        },
    ) {
        DashboardPanel(it, uiState.dashboardData, onIncrementUserCounter)
    }
}

@Composable
private fun DashboardPanel(
    contentPadding: PaddingValues,
    data: DashboardData,
    onIncrementUserCounter: (String) -> Unit,
) {
    Rn3LazyColumnFullScreen(
        Modifier.padding(8.dp),
        verticalArrangement = spacedBy(8.dp),
        contentPadding = contentPadding,
    ) {
        items(data.counters, key = { it.uid }) {
            it.DashboardCard(onIncrement = onIncrementUserCounter)
        }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        DashboardScreen(
            uiState = DashboardUiState(
                dashboardData = PreviewParameterData.dashboardData_default,
            ),
        )
    }
}
