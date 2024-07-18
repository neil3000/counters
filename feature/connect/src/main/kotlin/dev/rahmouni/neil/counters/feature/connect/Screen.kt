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
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.designsystem.BottomBarItem
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurface
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.HOME
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.rebased.User
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.shapes.MorphableShape
import dev.rahmouni.neil.counters.core.shapes.loadingShapeParameters
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.feature.connect.R.string
import kotlinx.coroutines.launch


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
        topAppBarActions = listOfNotNull(
            TopAppBarAction(
                Outlined.Settings,
                stringResource(string.feature_connect_topAppBarActions_settings),
                onSettingsTopAppBarActionClicked,
            ),
            feedbackTopAppBarAction,
        ),
        bottomBarItems = listOf(
            BottomBarItem(
                icon = Filled.Route,
                label = stringResource(string.feature_connect_bottomBar_connect),
                onClick = {},
                selected = true,
            ),
            BottomBarItem(
                icon = Filled.People,
                label = stringResource(string.feature_connect_bottomBar_friends),
                onClick = onFriendsBottomBarItemClicked,
            ),
            BottomBarItem(
                icon = Filled.Add,
                label = stringResource(string.feature_connect_bottomBar_add),
                onClick = onAddBottomBarItemClicked,
                unselectedIconColor = Color(color = 0xFFE8175D),
                fullSize = true,
            ),
            BottomBarItem(
                icon = Filled.Place,
                label = stringResource(string.feature_connect_bottomBar_local),
                onClick = onLocalBottomBarItemClicked,
            ),
            BottomBarItem(
                icon = Filled.Event,
                label = stringResource(string.feature_connect_bottomBar_events),
                onClick = onEventsBottomBarItemClicked,
            ),
        ),
        topAppBarStyle = HOME,
    ) {
        ColumnPanel(it)
    }
}

@Composable
private fun ColumnPanel(paddingValues: Rn3PaddingValues) {
    val morphCursor by remember { mutableIntStateOf(0) }
    val morphProgress = remember { Animatable(0f) }

    val animatedRotation = remember { Animatable(0f) }

    val users = UserRepository.users

    LaunchedEffect(Unit) {
        launch {
            animatedRotation.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    tween(40000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            Modifier
                .widthIn(max = 200.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(
                    MorphableShape(
                        loadingShapeParameters[morphCursor]
                            .genShape(animatedRotation.value)
                            .normalized(),
                        loadingShapeParameters[(morphCursor + 1) % loadingShapeParameters.size]
                            .genShape(animatedRotation.value)
                            .normalized(),
                        morphProgress.value,
                    ),
                )
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Image(
                painter = painterResource(id = R.drawable.feature_connect_pfp),
                contentDescription = "Neïl Rahmouni",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize(),
            )
        }

        Row(modifier = Modifier.padding(vertical = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Neïl Rahmouni",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                fontSize = TextUnit(5f, TextUnitType.Em),
            )
            Rn3IconButton(icon = Outlined.Edit, contentDescription = "edit profile", contentColor = MaterialTheme.colorScheme.primary) {
            }
        }
        users.forEach { user ->
            Rn3ExpandableSurface(
                content = {
                    Icon(Outlined.AccountCircle, null)
                    Spacer(Modifier.width(16.dp))
                    Text(user.name)
                },
                expandedContent = {
                    Text(
                        text = "",
                        Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                    )
                },
            )
        }
    }
}


object UserRepository {
    val users = listOf(
        User(
            userId = "1",
            name = "Alice Smith",
            email = "alice.smith@example.com",
            phone = "123-456-7890",
        ),
        User(
            userId = "2",
            name = "Bob Johnson",
            email = "bob.johnson@example.com",
            phone = "234-567-8901",
        ),
        User(
            userId = "3",
            name = "Carol Williams",
            email = "carol.williams@example.com",
            phone = "345-678-9012",
        ),
        User(
            userId = "4",
            name = "David Brown",
            email = "david.brown@example.com",
            phone = "456-789-0123",
        ),
        User(
            userId = "5",
            name = "Eva Davis",
            email = "eva.davis@example.com",
            phone = "567-890-1234",
        ),
    )
}
