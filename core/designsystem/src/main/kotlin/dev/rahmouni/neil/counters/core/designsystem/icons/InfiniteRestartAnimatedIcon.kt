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

package dev.rahmouni.neil.counters.core.designsystem.icons

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun Rn3InfiniteRestartAnimatedIcon(
    icon: AnimatedIcon,
    contentDescription: String?,
    delayBeforeRestart: LongRange = (icon.duration..icon.duration * 2),
) {
    var atEnd by remember { mutableStateOf(false) }

    val staticIcon = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(id = icon.drawable),
        atEnd = false,
    )
    val animatedIcon = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(id = icon.drawable),
        atEnd = atEnd,
    )

    LaunchedEffect(Unit) {
        while (true) {
            atEnd = !atEnd
            delay(timeMillis = icon.duration)
            atEnd = !atEnd
            delay(timeMillis = delayBeforeRestart.random())
        }
    }

    Icon(
        painter = if (atEnd) animatedIcon else staticIcon,
        contentDescription = contentDescription,
    )
}
