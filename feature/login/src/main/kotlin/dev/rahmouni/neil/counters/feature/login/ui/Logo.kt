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

package dev.rahmouni.neil.counters.feature.login.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.EaseInOutQuint
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import dev.rahmouni.neil.counters.core.designsystem.LocalNavAnimatedVisibilityScope
import dev.rahmouni.neil.counters.core.designsystem.LocalSharedTransitionScope
import dev.rahmouni.neil.counters.core.designsystem.R.color
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.icons.Logo
import kotlin.math.absoluteValue

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
internal fun Logo() {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("RahNeil_N3:4F6o9kodw29Oaj8zoDlAWesB1Merqam9")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("RahNeil_N3:743RaDiJYkZUoAmVqvPrWsm6BgQ9h78Y")

    var trigger by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) { trigger = true }
    val shinyPosition: Dp by animateDpAsState(
        targetValue = if (trigger) 45.dp else (-45).dp,
        animationSpec = tween(delayMillis = 3000, durationMillis = 1500, easing = EaseInOutQuint),
        label = "Shape rotation animation",
    )

    with(sharedTransitionScope) {
        Surface(
            modifier = Modifier
                .size(80.dp)
                .sharedElement(
                    state = rememberSharedContentState(key = "Logo_background"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { initialBounds, targetBounds ->
                        keyframes {
                            initialBounds at 0 using ArcMode.ArcBelow using EaseInOutQuint
                            targetBounds at durationMillis
                        }
                    },
                ),
            color = Color(color = ContextCompat.getColor(context, color.core_designsystem_color)),
            shape = Rn3SurfaceDefaults.shape,
        ) {
            Box(modifier = Modifier.size(80.dp)) {
                if (shinyPosition.value.absoluteValue != 45f) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset {
                                IntOffset(
                                    shinyPosition.roundToPx(),
                                    shinyPosition.roundToPx(),
                                )
                            }
                            .rotate(-45f)
                            .size(80.dp, 12.dp)
                            .scale(2f)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = .3f),
                                        Color.Transparent,
                                    ),
                                ),
                            ),
                    ) {}
                }

                with(animatedVisibilityScope) {
                    Modifier
                        .fillMaxSize()
                        .animateEnterExit(
                            enter = slideInVertically(
                                animationSpec = tween(
                                    delayMillis = 250,
                                    easing = EaseOutBack,
                                ),
                            ) { it } + fadeIn(
                                tween(
                                    durationMillis = 1,
                                    delayMillis = 250,
                                ),
                            ),
                        ).sharedElement(
                            rememberSharedContentState(key = "Logo_icon"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { initialBounds, targetBounds ->
                                keyframes {
                                    initialBounds at 0 using ArcMode.ArcBelow using EaseInOutQuint
                                    targetBounds at durationMillis
                                }
                            },
                        ).let { modifier ->
                            when {
                                else -> Icon(
                                    imageVector = Icons.Outlined.Logo,
                                    contentDescription = null,
                                    modifier = modifier.scale(.75f),
                                    tint = Color.White,
                                )
                            }
                        }
                }
            }
        }
    }
}
