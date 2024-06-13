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

package dev.rahmouni.neil.counters.feature.login

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.EaseInOutQuint
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material.icons.outlined.PlusOne
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.auth.LocalAuthHelper
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.LoggedOutUser
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.core.auth.user.UserAvatarAndName
import dev.rahmouni.neil.counters.core.designsystem.BuildConfig
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.NONE
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3
import dev.rahmouni.neil.counters.core.designsystem.roundedCorners.Rn3RoundedCornersSurfaceGroup
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun LoginRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    navigateToDashboard: () -> Unit,
) {
    val auth = LocalAuthHelper.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LoginScreen(
        modifier,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "LoginScreen",
            "KjMSjlWvIFeFt7kp2IPuTAU3yKkdyTqY",
        ).toTopAppBarAction(navController::navigateToFeedback),
        onConfirmSignIn = { anonymous ->
            if (anonymous) {
                scope.launch {
                    auth.signIn(context, true)
                }
            }
            navigateToDashboard()
        },
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onConfirmSignIn: (anonymous: Boolean) -> Unit = {},
) {
    Rn3Scaffold(
        modifier,
        "",
        null,
        listOfNotNull(feedbackTopAppBarAction),
        topAppBarStyle = NONE,
    ) {
        LoginPanel(onConfirmSignIn)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
private fun LoginPanel(
    onConfirmSignIn: (anonymous: Boolean) -> Unit,
) {
    val inPreview = LocalInspectionMode.current
    var trigger by rememberSaveable { mutableStateOf(inPreview) }
    val auth = LocalAuthHelper.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val haptics = getHaptic()

    val user by auth.getUserFlow().collectAsStateWithLifecycle(LoggedOutUser)

    LaunchedEffect(Unit) {
        delay(100)
        trigger = true
    }

    SharedTransitionLayout {
        AnimatedContent(
            trigger,
            label = "splashScreen to loginScreen SET",
        ) { triggerVisibility ->
            if (triggerVisibility) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Surface(
                        Modifier
                            .size(80.dp)
                            .sharedElement(
                                rememberSharedContentState(key = "background"),
                                animatedVisibilityScope = this@AnimatedContent,
                                boundsTransform = { initialBounds, targetBounds ->
                                    keyframes {
                                        initialBounds at 0 using ArcMode.ArcBelow using EaseInOutQuint
                                        targetBounds at durationMillis
                                    }
                                },
                            ),
                        color = Color(136, 18, 41),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Modifier
                            .skipToLookaheadSize()
                            .animateEnterExit(
                                enter = slideInVertically(
                                    animationSpec = tween(
                                        delayMillis = 250,
                                        easing = EaseOutBack,
                                    ),
                                ) { it } + fadeIn(tween(durationMillis = 1, delayMillis = 250)),
                            ).let { modifier ->
                                when {
                                    BuildConfig.DEBUG -> Icon(
                                        Icons.Outlined.Rn3,
                                        null,
                                        modifier.scale(.6f),
                                        tint = Color.White,
                                    )

                                    else -> Icon(
                                        Icons.Outlined.PlusOne,
                                        null,
                                        modifier.scale(.75f),
                                        tint = Color.White,
                                    )
                                }
                            }
                    }

                    Text(
                        "Welcome to Counters!",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Spacer(Modifier.height(48.dp))

                    Rn3TileSmallHeader(
                        Modifier.fillMaxWidth(),
                        "Continue with:",
                    )
                    Rn3RoundedCornersSurfaceGroup {
                        item {
                            Surface(tonalElevation = 8.dp, shape = it.toComposeShape()) {
                                Rn3TileClick(
                                    title = "Without an account",
                                    icon = Icons.Outlined.NoAccounts,
                                    supportingContent = { Text("You can sign in later anytime") },
                                    trailingContent = {
                                        Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null)
                                    },
                                    onClick = { onConfirmSignIn(true) },
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Rn3RoundedCornersSurfaceGroup {
                        item(user is SignedInUser) {
                            Surface(tonalElevation = 8.dp, shape = it.toComposeShape()) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            onClick = {
                                                haptics.click()
                                                onConfirmSignIn(false)
                                            },
                                        )
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    user.UserAvatarAndName(showEmail = true)
                                    Icon(
                                        Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                                        null,
                                    )
                                }
                            }
                        }
                        item {
                            Surface(tonalElevation = 8.dp, shape = it.toComposeShape()) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            haptics.click()
                                            scope.launch {
                                                auth.signIn(context, false)
                                            }
                                        }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Outlined.Add,
                                        null,
                                    )
                                    Spacer(Modifier.width(16.dp))
                                    Column {
                                        Text("Add an account")
                                        AnimatedVisibility(
                                            visible = user !is SignedInUser,
                                            exit = fadeOut() + shrinkVertically(
                                                animationSpec = tween(
                                                    delayMillis = 50,
                                                ),
                                            ),
                                        ) {
                                            Text(
                                                "Sign in to sync your data across devices",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                with(this@SharedTransitionLayout) {
                    Box(
                        Modifier
                            .background(Color(136, 18, 41))
                            .sharedElement(
                                rememberSharedContentState(key = "background"),
                                animatedVisibilityScope = this@AnimatedContent,
                            ),
                    ) {
                        Spacer(Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        LoginScreen()
    }
}
