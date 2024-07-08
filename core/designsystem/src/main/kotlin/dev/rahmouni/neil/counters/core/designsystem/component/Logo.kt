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

package dev.rahmouni.neil.counters.core.designsystem.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExposurePlus2
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.BuildConfig
import dev.rahmouni.neil.counters.core.designsystem.LocalNavAnimatedVisibilityScope
import dev.rahmouni.neil.counters.core.designsystem.LocalSharedTransitionScope
import dev.rahmouni.neil.counters.core.designsystem.icons.Logo
import kotlin.math.absoluteValue

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
fun Logo(modifier: Modifier = Modifier, shape: RoundedCornerShape = RoundedCornerShape(8.dp)) {
    val config = LocalConfigHelper.current
    val ee1 = (1..1000).random() == 1 || config.getBoolean("ee_1_force")

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("RahNeil_N3:4F6o9kodw29Oaj8zoDlAWesB1Merqam9")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("RahNeil_N3:743RaDiJYkZUoAmVqvPrWsm6BgQ9h78Y")

    var trigger by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) { trigger = true }
    val shinyPosition: Dp by animateDpAsState(
        if (trigger) 45.dp else (-45).dp,
        animationSpec = tween(delayMillis = 3000, durationMillis = 1500, easing = EaseInOutQuint),
        label = "Shape rotation animation",
    )

    with(sharedTransitionScope) {
        Surface(
            modifier = modifier
                .sharedElement(
                    rememberSharedContentState(key = "countersLogo_background"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { initialBounds, targetBounds ->
                        keyframes {
                            initialBounds at 0 using ArcMode.ArcBelow using EaseInOutQuint
                            targetBounds at durationMillis
                        }
                    },
                ),
            color = Color(color = 0xFFE8175D),
            shape = shape,
        ) {
            Box(modifier) {
                if (shinyPosition.value.absoluteValue != 45f) {
                    Box(
                        Modifier
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
                            rememberSharedContentState(key = "countersLogo_icon"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { initialBounds, targetBounds ->
                                keyframes {
                                    initialBounds at 0 using ArcMode.ArcBelow using EaseInOutQuint
                                    targetBounds at durationMillis
                                }
                            },
                        ).let { modifier ->
                            when {
                                BuildConfig.DEBUG -> Icon(
                                    Icons.Outlined.Logo,
                                    null,
                                    modifier.scale(.6f),
                                    tint = Color.White,
                                )

                                ee1 -> Icon(
                                    Icons.Outlined.ExposurePlus2,
                                    null,
                                    modifier.scale(.75f),
                                    tint = Color.White,
                                )

                                else -> Icon(
                                    Icons.Outlined.Logo,
                                    null,
                                    modifier.scale(.75f),
                                    tint = Color.White,
                                )
                            }
                        }
                }
            }
        }
    }
}
