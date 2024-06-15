package dev.rahmouni.neil.counters.core.designsystem

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically

fun rn3ExpandVerticallyTransition() = fadeIn(
    animationSpec = tween(
        delayMillis = 50,
    ),
) + expandVertically()

fun rn3ShrinkVerticallyTransition() = fadeOut() + shrinkVertically(
    animationSpec = tween(
        delayMillis = 50,
    ),
)