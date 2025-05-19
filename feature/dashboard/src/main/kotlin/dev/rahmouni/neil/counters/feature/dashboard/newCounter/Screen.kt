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

package dev.rahmouni.neil.counters.feature.dashboard.newCounter

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.feature.dashboard.R
import dev.rahmouni.neil.counters.feature.dashboard.newCounter.model.NewCounterUiState
import dev.rahmouni.neil.counters.feature.dashboard.newCounter.model.NewCounterUiState.*
import dev.rahmouni.neil.counters.feature.dashboard.newCounter.model.NewCounterViewModel
import dev.rahmouni.neil.counters.feature.dashboard.newCounter.ui.PickingCategoryPanel

@Composable
internal fun NewCounterRoute(
    modifier: Modifier = Modifier,
    viewModel: NewCounterViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NewCounterScreen(
        modifier,
        uiState,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "NewCounterScreen",
            "GSHW3EGUkwN79kpQ0MNxP1hzXQuozVuJ",
        ).toTopAppBarAction(navController::navigateToFeedback),
    )

    TrackScreenViewEvent(screenName = "NewCounter")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun NewCounterScreen(
    modifier: Modifier = Modifier,
    uiState: NewCounterUiState,
    feedbackTopAppBarAction: TopAppBarAction? = null,
) {
    Rn3Scaffold(
        modifier,
        stringResource(R.string.feature_dashboard_newCounterScreen_topAppBarTitle),
        null,
        listOfNotNull(feedbackTopAppBarAction),
    ) {
        when (uiState) {
            is PickingCategory -> PickingCategoryPanel(it)
        }
    }
}