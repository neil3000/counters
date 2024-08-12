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

package dev.rahmouni.neil.counters.core.designsystem

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnimatedNumber(
    modifier: Modifier = Modifier,
    currentValue: Double,
    content: @Composable (AnimatedContentScope.(targetState: Double) -> Unit),
) {
    AnimatedContent(
        modifier = modifier,
        targetState = currentValue,
        transitionSpec = {
            if (targetState > initialState) {
                (
                        slideInVertically { height -> (height / 1.8f).toInt() } +
                                fadeIn(animationSpec = tween(durationMillis = 200))
                        ).togetherWith(
                        exit = slideOutVertically { height -> -(height / 1.8f).toInt() } +
                                fadeOut(animationSpec = tween(durationMillis = 150)),
                    )
            } else {
                (
                        slideInVertically { height -> -(height / 1.8f).toInt() } +
                                fadeIn(animationSpec = tween(durationMillis = 200))
                        ).togetherWith(
                        exit = slideOutVertically { height -> (height / 1.8f).toInt() } +
                                fadeOut(animationSpec = tween(durationMillis = 150)),
                    )
            }.using(SizeTransform(clip = false))
        },
        label = "animated number",
        content = content,
    )
}
