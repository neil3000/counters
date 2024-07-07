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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.shapes.MorphableShape
import dev.rahmouni.neil.counters.core.shapes.loadingShapeParameters
import dev.rahmouni.neil.counters.feature.aboutme.R
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PfpData
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PfpData.LocalImage
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PfpData.RemoteImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoadingPfp(
    modifier: Modifier = Modifier,
    pfp: PfpData?,
    finishedLoadingAnimation: Boolean,
    onFinishLoadingAnimation: () -> Unit,
) {
    val haptic = getHaptic()

    var morphCursor by remember { mutableIntStateOf(0) }
    val morphProgress = remember { Animatable(0f) }

    val animatedRotation = remember { Animatable(0f) }

    var hasFinishedLoading by remember { mutableStateOf(pfp != null) }
    LaunchedEffect(pfp) {
        hasFinishedLoading = pfp != null
    }

    LaunchedEffect(Unit) {
        while (!hasFinishedLoading) {
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

                if (hasFinishedLoading && morphCursor > 2) {
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

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(visible = finishedLoadingAnimation) {
            Text(
                text = "Neïl Rahmouni",
                Modifier.padding(vertical = 24.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                fontSize = TextUnit(5f, TextUnitType.Em),
            )
        }

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
            this@Column.AnimatedVisibility(
                visible = hasFinishedLoading && morphCursor > 1 || finishedLoadingAnimation,
                enter = fadeIn(animationSpec = tween(700)),
            ) {
                if (pfp != null) {
                    Image(
                        painter = when (pfp) {
                            is LocalImage -> painterResource(id = R.drawable.feature_aboutme_pfp)
                            is RemoteImage -> rememberAsyncImagePainter(pfp.url)
                        },
                        contentDescription = "Neïl Rahmouni",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
