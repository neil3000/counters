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
        topStart = topStart + roundedCorners.topStart,
        topEnd = topEnd + roundedCorners.topEnd,
        bottomStart = bottomStart + roundedCorners.bottomStart,
        bottomEnd = bottomEnd + roundedCorners.bottomEnd,
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
