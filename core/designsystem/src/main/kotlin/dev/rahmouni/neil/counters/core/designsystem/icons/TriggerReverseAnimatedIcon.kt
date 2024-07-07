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

package dev.rahmouni.neil.counters.core.designsystem.icons

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
@JvmName("Rn3TriggerReverseAnimatedIcon_InteractionSource")
fun Rn3TriggerReverseAnimatedIcon(
    icon: AnimatedIcon,
    contentDescription: String?,
    interactionSource: @Composable () -> InteractionSource,
) {
    Rn3TriggerReverseAnimatedIcon(
        icon = icon,
        contentDescription = contentDescription,
        atEnd = { interactionSource().collectIsHoveredAsState().value || interactionSource().collectIsFocusedAsState().value || interactionSource().collectIsPressedAsState().value },
    )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun Rn3TriggerReverseAnimatedIcon(
    icon: AnimatedIcon,
    contentDescription: String?,
    atEnd: @Composable () -> Boolean,
) {
    Icon(
        painter = rememberAnimatedVectorPainter(
            animatedImageVector = AnimatedImageVector.animatedVectorResource(id = icon.drawable),
            atEnd = atEnd(),
        ),
        contentDescription = contentDescription,
    )
}
