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

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically

fun rn3ExpandVerticallyTransition(
    fadeDelay: Int = 50,
    fadeDuration: Int = 0,
    expandDelay: Int = 0,
    expandDuration: Int = 0,
) = fadeIn(
    animationSpec = tween(
        delayMillis = fadeDelay,
        durationMillis = fadeDuration,
    ),
) + expandVertically(
    animationSpec = tween(
        delayMillis = expandDelay,
        durationMillis = expandDuration,
    ),
)

fun rn3ShrinkVerticallyTransition(
    fadeDelay: Int = 0,
    fadeDuration: Int = 0,
    shrinkDelay: Int = 50,
    shrinkDuration: Int = 0,
) = fadeOut(
    animationSpec = tween(
        delayMillis = fadeDelay,
        durationMillis = fadeDuration,
    ),
) + shrinkVertically(
    animationSpec = tween(
        delayMillis = shrinkDelay,
        durationMillis = shrinkDuration,
    ),
)
