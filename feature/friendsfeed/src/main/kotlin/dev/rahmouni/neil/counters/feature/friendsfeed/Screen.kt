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

package dev.rahmouni.neil.counters.feature.friendsfeed

import Post
import Publication
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.designsystem.BottomBarItem
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.HOME
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDividerDefaults
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.feature.friendsfeed.R.string
import java.time.LocalDateTime

@Composable
internal fun FriendsFeedRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToConnect: () -> Unit,
    navigateToPublication: () -> Unit,
    navigateToLocal: () -> Unit,
    navigateToEvents: () -> Unit,
) {
    FriendsFeedScreen(
        modifier,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "FriendsFeedScreen",
            "GSChPsxOkpZYCqOdHXt9iZkNwdm0oJNj",
        ).toTopAppBarAction(navController::navigateToFeedback),
        onSettingsTopAppBarActionClicked = navigateToSettings,
        onConnectBottomBarItemClicked = navigateToConnect,
        onAddBottomBarItemClicked = navigateToPublication,
        onLocalBottomBarItemClicked = navigateToLocal,
        onEventsBottomBarItemClicked = navigateToEvents,
    )

    TrackScreenViewEvent(screenName = "FriendsFeed")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun FriendsFeedScreen(
    modifier: Modifier = Modifier,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onSettingsTopAppBarActionClicked: () -> Unit = {},
    onConnectBottomBarItemClicked: () -> Unit = {},
    onAddBottomBarItemClicked: () -> Unit = {},
    onLocalBottomBarItemClicked: () -> Unit = {},
    onEventsBottomBarItemClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_friendsfeed_topAppBarTitle),
        topAppBarTitleAlignment = CenterHorizontally,
        onBackIconButtonClicked = null,
        topAppBarActions = listOfNotNull(
            TopAppBarAction(
                Icons.Outlined.Settings,
                stringResource(string.feature_friendsfeed_topAppBarActions_settings),
                onSettingsTopAppBarActionClicked,
            ),
            feedbackTopAppBarAction,
        ),
        bottomBarItems = listOf(
            BottomBarItem(
                icon = Icons.Filled.Route,
                label = stringResource(string.feature_friendsfeed_bottomBar_connect),
                onClick = onConnectBottomBarItemClicked,
            ),
            BottomBarItem(
                icon = Icons.Filled.People,
                label = stringResource(string.feature_friendsfeed_bottomBar_friends),
                onClick = { },
                selected = true,
            ),
            BottomBarItem(
                icon = Icons.Filled.Add,
                label = stringResource(string.feature_friendsfeed_bottomBar_add),
                onClick = onAddBottomBarItemClicked,
                unselectedIconColor = Color(color = 0xFFE8175D),
                fullSize = true,
            ),
            BottomBarItem(
                icon = Icons.Filled.Place,
                label = stringResource(string.feature_friendsfeed_bottomBar_local),
                onClick = onLocalBottomBarItemClicked,
            ),
            BottomBarItem(
                icon = Icons.Filled.Event,
                label = stringResource(string.feature_friendsfeed_bottomBar_events),
                onClick = onEventsBottomBarItemClicked,
            ),
        ),
        topAppBarStyle = HOME,
    ) {
        LocalFeedPanel(
            it,
        )
    }
}

@Composable
private fun LocalFeedPanel(
    paddingValues: Rn3PaddingValues,
) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
    ) {
        listOf(
            Post(
                timestamp = LocalDateTime.now(),
                title = "Rediscover Street King Charles",
                content = "The Street King Charles was under construction, but now it's all clear! The renovation has finished, making this spot more accessible and enjoyable. Explore the new look of this iconic street and join me on this adventure.",
                place = "Street King Charles",
                placeIcon = Icons.Filled.Directions,
            ),
            Post(
                timestamp = LocalDateTime.now().minusMinutes(30),
                title = "Bike for Sale in London",
                content = "I'm selling my road bike in excellent condition! It's perfect for anyone looking to explore the city or commute efficiently. Details: Brand - Trek, Model - Emonda, Year - 2020, Color - Black. Contact me if interested!",
                place = "London",
                placeIcon = Icons.Filled.LocationCity,
                buttons = listOf("I'm interested"),
            ),
            Post(
                timestamp = LocalDateTime.now().minusHours(3),
                title = "Celebrating England's Newest National Park",
                content = "Exciting news for nature enthusiasts! England welcomes its newest national park, providing vast spaces for hiking, wildlife exploration, and stunning scenery. Discover the endless trails and the beauty of our protected lands. Join us in celebrating this great addition to our national heritage.",
                place = "England",
                placeIcon = Icons.Filled.Flag,
            ),
            Post(
                timestamp = LocalDateTime.now().minusDays(1),
                title = "Noisy Neighbors",
                content = "Seems like there's an impromptu concert every night next door! The music and noise levels from my neighbors have become a real challenge.",
                place = "221B Baker Street",
                placeIcon = Icons.Filled.Home,
            ),
            Post(
                timestamp = LocalDateTime.now().minusDays(5),
                title = "Power Outage in Chelsea",
                content = "Is anyone else experiencing a power outage in Chelsea?",
                place = "Chelsea",
                placeIcon = Icons.Filled.Place,
                buttons = listOf("Yes", "No"),
            ),
        ).forEach { post ->
            Publication(post)
            Rn3TileHorizontalDivider(
                paddingValues = Rn3TileHorizontalDividerDefaults.paddingValues.copy(
                    top = 0.dp,
                    bottom = 0.dp,
                ),
            )
        }
    }
}