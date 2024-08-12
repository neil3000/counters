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
