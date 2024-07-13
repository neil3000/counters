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

package dev.rahmouni.neil.counters.feature.connect

import androidx.annotation.VisibleForTesting
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.designsystem.BottomBarItem
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.HOME
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.feature.connect.R.string

@Composable
internal fun ConnectRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToFriends: () -> Unit,
    navigateToPublication: () -> Unit,
    navigateToLocal: () -> Unit,
    navigateToEvents: () -> Unit,
) {

    ConnectScreen(
        modifier,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "ConnectScreen",
            "oujWHHHpuFbChUEYhyGX39V2exJ299Dw",
        ).toTopAppBarAction(navController::navigateToFeedback),
        onSettingsTopAppBarActionClicked = navigateToSettings,
        onFriendsBottomBarItemClicked = navigateToFriends,
        onAddBottomBarItemClicked = navigateToPublication,
        onLocalBottomBarItemClicked = navigateToLocal,
        onEventsBottomBarItemClicked = navigateToEvents,
    )

    TrackScreenViewEvent(screenName = "connect")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun ConnectScreen(
    modifier: Modifier = Modifier,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onSettingsTopAppBarActionClicked: () -> Unit = {},
    onFriendsBottomBarItemClicked: () -> Unit = {},
    onAddBottomBarItemClicked: () -> Unit = {},
    onLocalBottomBarItemClicked: () -> Unit = {},
    onEventsBottomBarItemClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_connect_topAppBarTitle),
        topAppBarTitleAlignment = CenterHorizontally,
        onBackIconButtonClicked = null,
        topAppBarActions =  listOfNotNull(
            TopAppBarAction(
                Icons.Outlined.Settings,
                stringResource(string.feature_connect_topAppBarActions_settings),
                onSettingsTopAppBarActionClicked,
            ),
            feedbackTopAppBarAction,
        ),
        bottomBarItems = listOf(
            BottomBarItem(
                icon = Icons.Filled.Route,
                label = stringResource(string.feature_connect_bottomBar_connect),
                onClick = {},
                selected = true,
            ),
            BottomBarItem(
                icon = Icons.Filled.People,
                label = stringResource(string.feature_connect_bottomBar_friends),
                onClick = onFriendsBottomBarItemClicked,
            ),
            BottomBarItem(
                icon = Icons.Filled.Add,
                label = stringResource(string.feature_connect_bottomBar_add),
                onClick = onAddBottomBarItemClicked,
                unselectedIconColor = Color(color = 0xFFE8175D),
                fullSize = true,
            ),
            BottomBarItem(
                icon = Icons.Filled.Place,
                label = stringResource(string.feature_connect_bottomBar_local),
                onClick = onLocalBottomBarItemClicked,
            ),
            BottomBarItem(
                icon = Icons.Filled.Event,
                label = stringResource(string.feature_connect_bottomBar_events),
                onClick = onEventsBottomBarItemClicked,
            ),
        ),
        topAppBarStyle = HOME,
    ) {
    }
}