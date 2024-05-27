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
                                fadeOut(tween(250)),
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