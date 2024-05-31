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

@Composable
fun AnimatedNumber(
    currentValue: Long,
    content: @Composable (AnimatedContentScope.(targetState: Long) -> Unit),
) {
    AnimatedContent(
        targetState = currentValue,
        transitionSpec = {
            if (targetState > initialState) {
                (
                        slideInVertically { height -> (height / 1.8f).toInt() } +
                                fadeIn(tween(250))
                        ).togetherWith(
                        slideOutVertically { height -> -(height / 1.8f).toInt() } +
                                fadeOut(tween(200)),
                    )
            } else {
                (
                        slideInVertically { height -> -(height / 1.8f).toInt() } +
                                fadeIn(tween(250))
                        ).togetherWith(
                        slideOutVertically { height -> (height / 1.8f).toInt() } +
                                fadeOut(tween(250)),
                    )
            }.using(SizeTransform(clip = false))
        },
        label = "animated number",
        content = content,
    )
}
