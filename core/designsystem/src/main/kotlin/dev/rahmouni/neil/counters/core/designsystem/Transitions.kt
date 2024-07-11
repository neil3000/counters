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
