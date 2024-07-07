/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
