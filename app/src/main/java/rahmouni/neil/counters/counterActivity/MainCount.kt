package rahmouni.neil.counters.counterActivity

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.value_types.ValueType
import kotlin.math.absoluteValue

@Composable
fun MainCount(
    count: Int,
    valueType: ValueType
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var started by rememberSaveable { mutableStateOf(false) }
    var click by rememberSaveable { mutableStateOf(0) }
    val rotation: Float by animateFloatAsState(
        360 - click * 90f + (if (started) 0f else 30f),
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )
    val scale: Float by animateFloatAsState(
        if (started) 1f else .65f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(Unit) {
        started = true
    }

    Surface(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 24.dp)
            .size(112.dp)
            .rotate(rotation.absoluteValue)
            .scale(scale.absoluteValue)
            .clip(Shape1())
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                if (click == 3) {
                    click = 0
                } else click++
            }) {
            Box(
                Modifier.rotate(-rotation.absoluteValue),
                contentAlignment = Alignment.Center,
            ) {
                Row(Modifier.padding(bottom = 4.dp)) {
                    valueType.largeDisplay(this, count, context, false)
                }
            }
        }
    }
}