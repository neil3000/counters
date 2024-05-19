package dev.rahmouni.neil.counters.core.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
internal fun FeedbackMessages(messages: List<String>) {

    var trigger by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        trigger = true
    }

    Column(
        Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        messages.forEachIndexed { index, message ->
            AnimatedVisibility(
                visible = trigger,
                enter = if (index > 0) expandVertically(
                    tween(
                        250,
                        delayMillis = index * 1000,
                    ),
                ) + fadeIn(tween(250, delayMillis = index * 1000)) else fadeIn(),
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = message,
                        Modifier.padding(vertical = 6.dp, horizontal = 16.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        }
    }
}