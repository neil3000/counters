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
                                fadeIn(tween(200))
                        ).togetherWith(
                        slideOutVertically { height -> -(height / 1.8f).toInt() } +
                                fadeOut(tween(150)),
                    )
            } else {
                (
                        slideInVertically { height -> -(height / 1.8f).toInt() } +
                                fadeIn(tween(200))
                        ).togetherWith(
                        slideOutVertically { height -> (height / 1.8f).toInt() } +
                                fadeOut(tween(150)),
                    )
            }.using(SizeTransform(clip = false))
        },
        label = "animated number",
        content = content,
    )
}
