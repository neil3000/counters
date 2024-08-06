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

import android.content.Context
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dev.rahmouni.neil.counters.core.data.model.FriendEntity
import dev.rahmouni.neil.counters.core.data.model.FriendRawData
import dev.rahmouni.neil.counters.core.designsystem.BottomBarItem
import dev.rahmouni.neil.counters.core.designsystem.R.color
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3TextDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.HOME
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.icons.HumanGreetingProximity
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.END
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.model.data.Country
import dev.rahmouni.neil.counters.core.shapes.MorphableShape
import dev.rahmouni.neil.counters.core.shapes.loadingShapeParameters
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.core.user.Rn3User.Loading.getDisplayName
import dev.rahmouni.neil.counters.core.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.feature.connect.R.string
import dev.rahmouni.neil.counters.feature.connect.model.ConnectUiState.Loading
import dev.rahmouni.neil.counters.feature.connect.model.ConnectUiState.Success
import dev.rahmouni.neil.counters.feature.connect.model.ConnectViewModel
import dev.rahmouni.neil.counters.feature.connect.model.data.ConnectData
import dev.rahmouni.neil.counters.feature.connect.ui.Rn3FriendTileClick
import dev.rahmouni.neil.counters.feature.connect.ui.newFriendModal
import kotlinx.coroutines.launch

@Composable
internal fun ConnectRoute(
    modifier: Modifier = Modifier,
    viewModel: ConnectViewModel = hiltViewModel(),
    navController: NavController,
    navigateToSettings: () -> Unit,
    navigateToFriends: () -> Unit,
    navigateToPublication: () -> Unit,
    navigateToPublic: () -> Unit,
    navigateToEvents: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        Loading -> {}
        is Success -> ConnectScreen(
            modifier = modifier,
            data = (uiState as Success).connectData,
            feedbackTopAppBarAction = FeedbackScreenContext(
                localName = "ConnectScreen",
                localID = "oujWHHHpuFbChUEYhyGX39V2exJ299Dw",
            ).toTopAppBarAction(navController::navigateToFeedback),
            onSettingsTopAppBarActionClicked = navigateToSettings,
            onFriendsBottomBarItemClicked = navigateToFriends,
            onAddBottomBarItemClicked = navigateToPublication,
            onPublicBottomBarItemClicked = navigateToPublic,
            onEventsBottomBarItemClicked = navigateToEvents,
            onAccountTileLoginButtonClicked = navigateToLogin,
            onAddFriend = viewModel::addFriend,
        )
    }

    TrackScreenViewEvent(screenName = "connect")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun ConnectScreen(
    modifier: Modifier = Modifier,
    data: ConnectData,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onSettingsTopAppBarActionClicked: () -> Unit = {},
    onFriendsBottomBarItemClicked: () -> Unit = {},
    onAddBottomBarItemClicked: () -> Unit = {},
    onPublicBottomBarItemClicked: () -> Unit = {},
    onEventsBottomBarItemClicked: () -> Unit = {},
    onAccountTileLoginButtonClicked: () -> Unit = {},
    onAddFriend: (FriendRawData) -> Unit = {},
) {
    val haptic = getHaptic()
    val context = LocalContext.current

    val fabExpanded = remember { mutableStateOf(value = true) }

    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value }
            .collect { scrollPosition ->
                fabExpanded.value = scrollPosition == 0
            }
    }

    val openNewFriendModal = newFriendModal(onAddFriend = onAddFriend)

    val add = when (data.user) {
        is SignedInUser -> BottomBarItem(
            icon = Filled.Add,
            label = stringResource(string.feature_connect_bottomBar_add),
            onClick = onAddBottomBarItemClicked,
            unselectedIconColor = Color(ContextCompat.getColor(context, color.core_designsystem_color)),
            fullSize = true,
        )

        else -> BottomBarItem(
            icon = Filled.Add,
            label = stringResource(string.feature_connect_bottomBar_add),
            onClick = {
                haptic.click()

                Toast
                    .makeText(
                        context,
                        context.getString(string.feature_connect_bottomBar_add_disabled),
                        Toast.LENGTH_SHORT,
                    )
                    .show()
            },
            unselectedIconColor = MaterialTheme.colorScheme.secondaryContainer,
            fullSize = true,
        )
    }

    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = stringResource(string.feature_connect_topAppBarTitle),
        topAppBarTitleAlignment = CenterHorizontally,
        onBackIconButtonClicked = null,
        topAppBarActions = listOfNotNull(
            TopAppBarAction(
                icon = Outlined.Settings,
                title = stringResource(string.feature_connect_topAppBarActions_settings),
                onClick = onSettingsTopAppBarActionClicked,
            ),
            feedbackTopAppBarAction,
        ),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = it,
                text = { Text(text = stringResource(string.feature_connect_fab_text)) },
                icon = { Icon(imageVector = Outlined.Add, contentDescription = null) },
                onClick = {
                    haptic.click()

                    openNewFriendModal()
                },
                expanded = fabExpanded.value,
            )
        },
        bottomBarItems = listOf(
            BottomBarItem(
                icon = Outlined.HumanGreetingProximity,
                label = stringResource(string.feature_connect_bottomBar_connect),
                onClick = {},
                selected = true,
            ),
            BottomBarItem(
                icon = Filled.People,
                label = stringResource(string.feature_connect_bottomBar_friends),
                onClick = onFriendsBottomBarItemClicked,
            ),
            add,
            BottomBarItem(
                icon = Filled.Public,
                label = stringResource(string.feature_connect_bottomBar_public),
                onClick = onPublicBottomBarItemClicked,
            ),
            BottomBarItem(
                icon = Filled.Event,
                label = stringResource(string.feature_connect_bottomBar_events),
                onClick = onEventsBottomBarItemClicked,
            ),
        ),
        topAppBarStyle = HOME,
    ) {
        ColumnPanel(
            paddingValues = it,
            data = data,
            context = context,
            onAccountTileLoginButtonClicked = onAccountTileLoginButtonClicked,
            scrollState = scrollState,
        )
    }
}

@Composable
private fun ColumnPanel(
    paddingValues: Rn3PaddingValues,
    data: ConnectData,
    context: Context,
    onAccountTileLoginButtonClicked: () -> Unit,
    scrollState: ScrollState,
) {
    val morphCursor by remember { mutableIntStateOf(value = 0) }
    val morphProgress = remember { Animatable(initialValue = 0f) }

    val animatedRotation = remember { Animatable(initialValue = 0f) }

    val phoneUtil = PhoneNumberUtil.getInstance();

    LaunchedEffect(Unit) {
        launch {
            animatedRotation.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 40000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            )
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(paddingValues.add(bottom = 80.dp)),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when (data.user) {
            is SignedInUser -> {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 150.dp)
                            .fillMaxWidth()
                            .aspectRatio(ratio = 1f)
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
                            .background(MaterialTheme.colorScheme.onPrimaryContainer),
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(data.user.pfpUri),
                            contentDescription = data.user.getDisplayName(context),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize(),
                        )
                    }

                    Row(
                        modifier = Modifier.padding(Rn3TextDefaults.paddingValues),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = data.user.getDisplayName(context),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            fontSize = TextUnit(value = 5f, type = TextUnitType.Em),
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

            else -> Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Rn3SurfaceDefaults.paddingValues),
                shape = Rn3SurfaceDefaults.shape,
                tonalElevation = Rn3SurfaceDefaults.tonalElevation,
            ) {
                Row(
                    modifier = Modifier
                        .padding(Rn3TextDefaults.paddingValues)
                        .defaultMinSize(minHeight = 44.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f).padding(Rn3TextDefaults.paddingValues.only(END))) {
                        Text(
                            text = getDisplayName(context),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = stringResource(string.feature_connect_loginTile_supportingtext),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    // loginButton
                    OutlinedButton(onClick = onAccountTileLoginButtonClicked) {
                        Text(text = stringResource(string.feature_connect_loginTile_button))
                    }
                }
            }
        }

        val sortedFriendEntities = data.friends.sortedWith(compareBy<FriendEntity> { !it.nearby }.thenBy { it.name })

        sortedFriendEntities.forEach { friend ->
            Rn3FriendTileClick(
                button = phoneUtil.isValidNumber(data.phone),
                friendEntity = friend,
            )
        }
    }
}
