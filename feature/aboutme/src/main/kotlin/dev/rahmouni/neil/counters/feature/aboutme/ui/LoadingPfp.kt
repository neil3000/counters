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

package dev.rahmouni.neil.counters.feature.aboutme.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.shapes.MorphableShape
import dev.rahmouni.neil.counters.core.shapes.loadingShapeParameters
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoadingPfp(
    finishedLoading: () -> Boolean,
    finishedLoadingAnimation: Boolean,
    onFinishLoadingAnimation: () -> Unit,
) {
    val haptic = getHaptic()

    var morphCursor by remember { mutableIntStateOf(0) }
    val morphProgress = remember { Animatable(0f) }

    val animatedRotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (!finishedLoading()) {
            repeat(loadingShapeParameters.size) {
                haptic.smallTick()
                morphProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = .6f,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                )
                delay(250)
                morphCursor = (morphCursor + 1) % loadingShapeParameters.size
                morphProgress.animateTo(0f, snap())

                if (finishedLoading() && morphCursor > 2) {
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
            }
        }
        haptic.longPress()
        onFinishLoadingAnimation()
    }

    Box(
        Modifier
            .fillMaxWidth(.4f)
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
        AnimatedVisibility(
            visible = finishedLoading() && morphCursor > 1 || finishedLoadingAnimation,
            enter = fadeIn(animationSpec = tween(700)),
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://lh3.googleusercontent.com/pw/AP1GczN5SMNjm_3K-WhJLL_0GCpgEn_XiB61-oVfsR1iObwuFtUGejnhK1FtOpGCb_weyj7AamPJKhOt1dcV6pRx6lM-z3ktd2BFdBZ7AOsMd3Tv4YrEGejWko7BZ_zpWwBnOC8VEIK9dk9AeuOJkmfvEQZlkA=w637-h637-s-no-gm?authuser=0"),
                contentDescription = "Neïl Rahmouni",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
