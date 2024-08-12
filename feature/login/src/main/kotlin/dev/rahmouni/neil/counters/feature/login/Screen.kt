/*
 * Copyright (C) 2024 Rahmouni Neïl & Aurélien Coppée
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

package dev.rahmouni.neil.counters.feature.login

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.analytics.AnalyticsEvent
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.auth.LocalAuthHelper
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3TextDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.TRANSPARENT
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3AdditionalPadding
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.HORIZONTAL
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.roundedCorners.Rn3RoundedCorners
import dev.rahmouni.neil.counters.core.designsystem.roundedCorners.Rn3RoundedCornersSurfaceGroup
import dev.rahmouni.neil.counters.core.designsystem.roundedCorners.Rn3RoundedCornersSurfaceGroupDefaults
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.shapes.Rn3Shapes
import dev.rahmouni.neil.counters.core.shapes.Shape
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.core.user.Rn3User
import dev.rahmouni.neil.counters.core.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.feature.login.R.string
import dev.rahmouni.neil.counters.feature.login.model.LoginViewModel
import dev.rahmouni.neil.counters.feature.login.ui.AddAccountTile
import dev.rahmouni.neil.counters.feature.login.ui.AnonymousTile
import dev.rahmouni.neil.counters.feature.login.ui.Logo
import dev.rahmouni.neil.counters.feature.login.ui.Tile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController,
    navigateToNextPage: () -> Unit,
) {
    val auth = LocalAuthHelper.current
    val context = LocalContext.current
    val analytics = LocalAnalyticsHelper.current
    val scope = rememberCoroutineScope()

    val user by viewModel.user.collectAsStateWithLifecycle()

    LoginScreen(
        modifier = modifier,
        user = user,
        feedbackTopAppBarAction = FeedbackScreenContext(
            localName = "LoginScreen",
            localID = "KjMSjlWvIFeFt7kp2IPuTAU3yKkdyTqY",
        ).toTopAppBarAction(navController::navigateToFeedback),
        onConfirmSignIn = { anonymous ->
            if (anonymous) {
                scope.launch {
                    auth.signIn(context, true)
                }
            }

            analytics.logEvent(
                AnalyticsEvent(
                    type = AnalyticsEvent.Types.LOGIN,
                    extras = listOf(
                        AnalyticsEvent.Param(
                            key = AnalyticsEvent.ParamKeys.METHOD,
                            value = if (anonymous) "anonymous" else "credential_manager",
                        ),
                    ),
                ),
            )

            viewModel.login()
            navigateToNextPage()
        },
        onAddAccountTileClicked = {
            scope.launch {
                auth.signIn(context = context, anonymously = false)
            }
        },
    )

    TrackScreenViewEvent(screenName = "Login")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    user: Rn3User,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onConfirmSignIn: (anonymous: Boolean) -> Unit = {},
    onAddAccountTileClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = "",
        onBackIconButtonClicked = null,
        topAppBarActions = listOfNotNull(feedbackTopAppBarAction),
        topAppBarStyle = TRANSPARENT,
    ) {
        LoginPanel(
            user = user,
            onConfirmSignIn = onConfirmSignIn,
            onAddAccountTileClicked = onAddAccountTileClicked,
        )
    }
}

@Composable
private fun LoginPanel(
    user: Rn3User,
    onConfirmSignIn: (anonymous: Boolean) -> Unit,
    onAddAccountTileClicked: () -> Unit,
) {
    val inPreview = LocalInspectionMode.current
    var trigger by rememberSaveable { mutableStateOf(value = inPreview) }

    LaunchedEffect(Unit) {
        delay(timeMillis = 1)
        trigger = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val bgShapeRotationPerpetual by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 80_000, easing = LinearEasing),
        ),
        label = "Shape rotation animation -- perpetual",
    )
    val bgShapeRotationIntroduction: Float by animateFloatAsState(
        targetValue = if (trigger) 180f else 0f,
        animationSpec = tween(durationMillis = 5000, easing = EaseOutCirc),
        label = "Shape rotation animation -- introduction",
    )

    Box {
        Box(
            modifier = Modifier
                .offset((-120).dp, (-80).dp)
                .width(300.dp)
                .aspectRatio(1f)
                .graphicsLayer {
                    rotationZ =
                        (bgShapeRotationIntroduction + bgShapeRotationPerpetual) / 2f
                }
                .clip(shape = Shape(polygon = Rn3Shapes.Scallop))
                .background(color = MaterialTheme.colorScheme.secondaryContainer),
        ) {}

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(30.dp, (-50).dp)
                .width(100.dp)
                .aspectRatio(1f)
                .graphicsLayer {
                    rotationZ = -bgShapeRotationIntroduction - bgShapeRotationPerpetual
                }
                .clip(shape = Shape(polygon = Rn3Shapes.ScallopPointy))
                .background(color = MaterialTheme.colorScheme.tertiaryContainer),
        ) {}

        Column(
            modifier = Modifier
                .padding(
                    Rn3AdditionalPadding.paddingValues.only(HORIZONTAL),
                )
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Logo()

            Text(
                text = stringResource(string.feature_login_welcomeMessage_start) + stringResource(
                    string.feature_login_welcomeMessage_appName,
                ) + stringResource(
                    string.feature_login_welcomeMessage_ending,
                ),
                modifier = Modifier.padding(Rn3TextDefaults.paddingValues),
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(Modifier.height(48.dp))
            Rn3TileSmallHeader(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(string.feature_login_choose),
                paddingValues = Rn3TextDefaults.paddingValues.copy(
                    start = 8.dp,
                    bottom = 10.dp,
                ),
            )
            AnonymousTile(shape = Rn3RoundedCorners(all = Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerExternal)) {
                onConfirmSignIn(true)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Rn3RoundedCornersSurfaceGroup {
                item(user is SignedInUser) {
                    user.Tile(shape = it) { onConfirmSignIn(false) }
                }
                item {
                    AddAccountTile(
                        expanded = user !is SignedInUser,
                        shape = it,
                        onClick = onAddAccountTileClicked,
                    )
                }
            }
        }
    }
}

/*@OptIn(ExperimentalSharedTransitionApi::class)
@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
            LoginScreen()
    }
}*/
