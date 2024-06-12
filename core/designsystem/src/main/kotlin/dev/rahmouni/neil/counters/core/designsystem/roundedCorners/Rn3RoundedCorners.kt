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

package dev.rahmouni.neil.counters.core.designsystem.roundedCorners

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Rn3RoundedCorners(
    val topStart: Dp = 0.dp,
    val topEnd: Dp = 0.dp,
    val bottomStart: Dp = 0.dp,
    val bottomEnd: Dp = 0.dp,
) {
    constructor(top: Dp = 0.dp, bottom: Dp = 0.dp) : this(
        topStart = top,
        topEnd = top,
        bottomStart = bottom,
        bottomEnd = bottom,
    )

    constructor(all: Dp) : this(
        topStart = all,
        topEnd = all,
        bottomStart = all,
        bottomEnd = all,
    )

    operator fun plus(roundedCorners: Rn3RoundedCorners): Rn3RoundedCorners = Rn3RoundedCorners(
        topStart + roundedCorners.topStart,
        topEnd + roundedCorners.topEnd,
        bottomStart + roundedCorners.bottomStart,
        bottomEnd + roundedCorners.bottomEnd,
    )

    fun toComposeShape(): RoundedCornerShape {
        return RoundedCornerShape(
            topStart = topStart,
            topEnd = topEnd,
            bottomStart = bottomStart,
            bottomEnd = bottomEnd,
        )
    }
}
